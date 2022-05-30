package com.dongzz.quick.security.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 在线用户信息缓存
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    private LoginUser loginUser; // 认证主体

    private String userName; // 账号
    private String browser; // 浏览器
    private String ip; // IP
    private String address; // 地址
    private String token; // 令牌 加密存储
    private Date loginTime; // 登录时间

}
