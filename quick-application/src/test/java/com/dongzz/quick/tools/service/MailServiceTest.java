package com.dongzz.quick.tools.service;

import com.dongzz.quick.SpringBootJunit;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.tools.service.dto.MailDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 邮件功能 测试
 */
public class MailServiceTest extends SpringBootJunit {

    @Autowired
    MailService mailService;

    /**
     * 去掉字符串首尾空格
     */
    @Test
    public void trimTest() {
        String str = " hello world";
        String trim = StringUtil.trim(str);
        System.out.println(trim);
    }

    /**
     * 发送邮件 yml配置
     */
    @Test
    public void sendTest() throws Exception {
        MailDto mailDto = new MailDto();
        mailDto.setId(1);
        mailDto.setSubject("一封测试邮件");
        mailDto.setContent("这是一封测试邮件，不必大惊小怪");
        mailDto.setToUsers("zwk20190520@163.com");
        mailService.send(mailDto);
    }
}
