package com.dongzz.quick.logging.aspect;

import cn.hutool.json.JSONUtil;
import com.dongzz.quick.common.utils.SecurityUtil;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.common.utils.WebUtil;
import com.dongzz.quick.logging.annotation.Log;
import com.dongzz.quick.logging.service.LogsService;
import com.dongzz.quick.logging.domain.SysLogs;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 统一日志处理
 *
 * @author zwk
 * @date 2022/5/18 21:49
 * @email zbcbbs@163.com
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogsService logsService;

    /**
     * 系统日志
     *
     * @param joinPoint 方法信息
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.dongzz.quick.logging.annotation.Log)")
    public Object logSys(ProceedingJoinPoint joinPoint) throws Throwable {
        // 在线用户
        UserDetails user = SecurityUtil.getCurrentUser();

        SysLogs logs = new SysLogs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取 @Log 注解内容
        if (signature.getMethod().isAnnotationPresent(Log.class)) {
            Log log = signature.getMethod().getAnnotation(Log.class);
            logs.setModule(log.module());
            logs.setContent(log.operate());
        }

        // 操作位置 类，方法
        Object target = joinPoint.getTarget();
        String className = target.getClass().getName();
        String methodName = target.getClass().getMethod(signature.getName(), signature.getParameterTypes()).getName();
        logs.setClazz(className);
        logs.setMethod(methodName);

        // ip
        HttpServletRequest request = WebUtil.getHttpRequest();
        String ip = StringUtil.getIp(request);
        logs.setIp(ip);

        // 请求路径
        logs.setUrl(request.getRequestURL().toString());

        // 请求参数
        Map<String, Object> params = WebUtil.getParamsMap(request);
        logs.setParam(JSONUtil.toJsonStr(params));

        // 统计耗时
        long startTime = System.currentTimeMillis();
        logs.setStartTime(new Date());
        try {
            // 方法执行
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logs.setEndTime(new Date());
            // 执行耗时
            logs.setTotalTime((int) (endTime - startTime));
            logs.setStatus("1"); // 操作成功

            // 返回值
            if (null != result) {
                logs.setReturnData(JSONUtil.toJsonStr(result));
            }
            return result;
        } catch (Exception e) {
            logs.setStatus("0"); // 操作异常
            logs.setContent(e.getMessage()); // 异常信息
            throw e;
        } finally {
            logs.setUser(user.getUsername());
            logs.setTime(new Date());
            logs.setIsDel("0"); // 未删除
            logsService.insertSelective(logs);
        }
    }

}
