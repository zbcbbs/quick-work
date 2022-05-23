package com.dongzz.quick.common.aspect;

import com.dongzz.quick.common.annotation.Limit;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.common.utils.WebUtil;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 接口限流
 *
 * @author zwk
 * @date 2022/5/17 10:15
 * @email zbcbbs@163.com
 */
@Aspect
@Component
public class LimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);
    private final RedisTemplate<Object, Object> redisTemplate;


    public LimitAspect(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.dongzz.quick.common.annotation.Limit)")
    public void pointcut() {

    }

    /**
     * 分布式限流 redis + lua
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = WebUtil.getHttpRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Limit limit = method.getAnnotation(Limit.class);
        Limit.LimitType limitType = limit.limitType();
        String key = limit.key();
        if (StringUtils.isEmpty(key)) {
            if (limitType == Limit.LimitType.IP) {
                key = StringUtil.getIp(request);
            } else {
                key = method.getName();
            }
        }
        // 不可变的 线程安全的集合
        ImmutableList<Object> keys = ImmutableList.of(StringUtils.join(limit.prefix(), "_", key, "_", request.getRequestURI().replace("/", "_")));
        // lua脚本
        String luaScript = buildLuaScript();
        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        // 获取令牌桶中的令牌
        Number count = redisTemplate.execute(redisScript, keys, limit.count(), limit.period());
        if (null != count && count.intValue() <= limit.count()) {
            logger.debug("第{}次访问接口，Key：{}，描述：[{}]", count, keys, limit.name());
            return joinPoint.proceed();
        } else {
            throw new ServiceException("访问过于频繁，您被限流了，请稍后重试");
        }
    }

    /**
     * Lua 限流脚本
     */
    private String buildLuaScript() {

        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}
