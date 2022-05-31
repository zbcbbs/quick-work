package com.dongzz.quick.tools.service;

import com.dongzz.quick.SpringBootJunit;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.tools.domain.ToolMailConfig;
import com.dongzz.quick.tools.service.dto.MailDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

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
    public void sendTestYml() throws Exception {
        MailDto mailDto = new MailDto();
        mailDto.setUserId(1);
        mailDto.setSubject("一封测试邮件");
        mailDto.setContent("这是一封测试邮件，不必大惊小怪");
        mailDto.setToUsers("zwk20190520@163.com");
        mailDto.setCreateTime(new Date());
        mailDto.setUpdateTime(new Date());
        MailDto mail = mailService.addMail(mailDto);
        mailService.send(mail);
    }

    /**
     * 发送邮件 mysql配置
     */
    @Test
    public void sendTestMysql() throws Exception {
        MailDto mailDto = new MailDto();
        mailDto.setUserId(1);
        mailDto.setSubject("一封测试邮件");
        mailDto.setContent("这是一封测试邮件，不必大惊小怪");
        mailDto.setToUsers("zwk20190520@163.com");
        mailDto.setCreateTime(new Date());
        mailDto.setUpdateTime(new Date());
        MailDto mail = mailService.addMail(mailDto);
        ToolMailConfig config = mailService.find();
        mailService.send(mail, config);
    }

    /**
     * 发送邮件 不记录日志
     */
    @Test
    public void sendTestSimple() throws Exception {
        MailDto mail = new MailDto();
        mail.setSubject("一封测试邮件");
        mail.setContent("这是一封测试邮件，不必大惊小怪");
        mail.setToUsers(" zwk 20190520@163.com;zbcbbs@163.com");
        ToolMailConfig config = mailService.find();
        mailService.sendSimple(mail, config);
    }
}
