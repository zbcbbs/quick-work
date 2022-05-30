package com.dongzz.quick.tools.service;

/**
 * 验证码服务接口
 *
 * @author zwk
 * @date 2022/5/28 20:49
 * @email zbcbbs@163.com
 */
public interface CodeService {

    /**
     * 邮箱验证码
     *
     * @param email
     * @throws Exception
     */
    void sendEmailCode(String email) throws Exception;

}
