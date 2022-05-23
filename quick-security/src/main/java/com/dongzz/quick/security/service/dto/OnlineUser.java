package com.dongzz.quick.security.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 缓存封装 在线用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser implements Serializable {

    private LoginUser loginUser; // 认证信息
    private String browser; // 浏览器
    private String ip; // IP
    private String address; // 地址
    private String token; // 令牌 加密存储
    private Date loginTime; // 登录时间

}
