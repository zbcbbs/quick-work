package com.dongzz.quick.tools.service;

/**
 * 短信 相关服务接口
 */
public interface SmsService {

    /**
     * 发送 短信验证码
     *
     * @param no     手机号
     * @param length 验证码长度
     * @return
     * @throws Exception
     */
    String sendSmsCode(String no, int length) throws Exception;

    /**
     * 验证 短信验证码
     *
     * @param no    手机号
     * @param input 用户输入的验证码
     * @return
     * @throws Exception
     */
    boolean validateSmsCode(String no, String input) throws Exception;
}
