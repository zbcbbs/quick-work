package com.dongzz.quick.security.service.dto;

import lombok.Data;

/**
 * 修改邮箱
 *
 * @author zwk
 * @date 2022/5/29 18:01
 * @email zbcbbs@163.com
 */
@Data
public class EmailDto {

    private String email;
    private String password;
    private String code; // 邮箱验证码
}
