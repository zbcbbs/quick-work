package com.dongzz.quick.security.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 在线用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineDto {

    private String userName; // 账号
    private String nickName; // 昵称
    private String dept; // 部门
    private String browser; // 浏览器
    private String ip; // IP
    private String address; // 地址
    private String token; // 令牌 加密存储
    private Date loginTime; // 登录时间
}
