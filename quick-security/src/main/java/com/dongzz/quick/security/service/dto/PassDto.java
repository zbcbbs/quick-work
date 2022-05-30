package com.dongzz.quick.security.service.dto;

import lombok.Data;

/**
 * 修改密码
 *
 * @author zwk
 * @date 2022/5/29 17:36
 * @email zbcbbs@163.com
 */
@Data
public class PassDto {

    private String oldPass;
    private String newPass;
}
