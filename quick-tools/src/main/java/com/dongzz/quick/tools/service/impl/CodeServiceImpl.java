package com.dongzz.quick.tools.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.utils.CacheKey;
import com.dongzz.quick.common.utils.RedisUtil;
import com.dongzz.quick.tools.service.CodeService;
import com.dongzz.quick.tools.service.MailService;
import com.dongzz.quick.tools.service.dto.MailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zwk
 * @date 2022/5/28 21:23
 * @email zbcbbs@163.com
 */
@Service
@Slf4j
public class CodeServiceImpl implements CodeService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MailService mailService;

    @Override
    public void sendEmailCode(String email) throws Exception {
        try {
            // 模板引擎
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
            Template template = engine.getTemplate("email/email.ftl");
            Map<String, Object> data = new HashMap<>();
            String code = RandomUtil.randomString("0123456789", 6);
            data.put("code", code);
            String content = template.render(data);
            MailDto mail = new MailDto();
            mail.setSubject("一封来自暴走编程的验证邮件");
            mail.setContent(content);
            mail.setToUsers(email);
            mailService.sendSimple(mail, mailService.find());
            // 缓存
            redisUtil.set(CacheKey.CODE_EMAIL + email, code, 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.debug("Email Verification Code Error:", e);
            throw new ServiceException("邮箱验证码发送异常！");
        }
    }

}
