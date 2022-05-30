package com.dongzz.quick.security.controller;

import cn.hutool.core.util.IdUtil;
import com.dongzz.quick.common.utils.RedisUtil;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.common.utils.SecurityUtil;
import com.dongzz.quick.security.config.bean.JwtProperties;
import com.dongzz.quick.security.config.bean.LoginCodeEnum;
import com.dongzz.quick.security.config.bean.LoginProperties;
import com.dongzz.quick.security.service.SecurityService;
import com.dongzz.quick.security.service.dto.LoginUser;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 鉴权认证
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "系统：鉴权认证", value = "鉴权认证相关接口")
public class AuthorizationController {

    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SecurityService securityService;

    /**
     * 获取验证码
     */
    @GetMapping("/code")
    @ApiOperation("获取验证码")
    public ResponseVo captcha() throws Exception {
        Captcha captcha = loginProperties.getCaptcha(); // 生成器
        String uuid = jwtProperties.getCodeKey() + IdUtil.simpleUUID();
        // 当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 缓存
        redisUtil.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 响应
        Map<String, Object> result = new HashMap<String, Object>(2) {{
            put("image", captcha.toBase64());
            put("uuid", uuid);
        }};
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
    }

    /**
     * 获取在线用户信息
     */
    @GetMapping("/info")
    @ApiOperation("获取在线用户信息")
    public ResponseVo info() throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtil.getCurrentUser();
        Map<String, Object> data = securityService.getCurrentUser(loginUser);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

}
