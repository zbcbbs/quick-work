package com.dongzz.quick.tools.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.SecurityUtil;
import com.dongzz.quick.tools.dao.ToolMailConfigMapper;
import com.dongzz.quick.tools.domain.ToolMail;
import com.dongzz.quick.tools.dao.ToolMailMapper;
import com.dongzz.quick.tools.domain.ToolMailConfig;
import com.dongzz.quick.tools.service.MailService;
import com.dongzz.quick.tools.service.dto.MailDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "mail")
public class MailServiceImpl extends BaseMybatisServiceImpl<ToolMail> implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private ToolMailMapper mailMapper;
    @Autowired
    private ToolMailConfigMapper mailConfigMapper;

    // 邮箱配置
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String mailServer;
    @Value("${spring.mail.user}")
    private String user;

    @Override
    @CachePut(key = "'config'")
    @Transactional
    public ToolMailConfig config(ToolMailConfig config) throws Exception {
        Integer id = config.getId();
        if (null != id) {
            // 修改
            mailConfigMapper.updateByPrimaryKeySelective(config);
        } else {
            // 新增
            config.setId(1);
            mailConfigMapper.insertSelective(config);
        }
        return config;
    }

    @Override
    @Cacheable(key = "'config'")
    public ToolMailConfig find() throws Exception {
        ToolMailConfig config = mailConfigMapper.selectByPrimaryKey(1);
        return config;
    }

    @Override
    public void send(MailDto mailDto) throws Exception {
        String toUsers = mailDto.getToUsers(); // 收件人邮箱，支持多个 ';'分割
        if (StringUtils.isBlank(toUsers)) {
            throw new ServiceException("收件人不能为空");
        }
        toUsers = toUsers.replace(" ", "").replace("；", ";");
        String[] tos = toUsers.split(";");

        for (String to : tos) {
            int status = 1;
            try {
                // 创建邮件并发送
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(user + "<" + mailServer + ">");
                helper.setTo(to);
                helper.setSubject(mailDto.getSubject());
                helper.setText(mailDto.getContent(), true);
                javaMailSender.send(message);
            } catch (Exception e) {
                status = 0;
                logger.debug("向 " + to + " 发送邮件失败！", e);
            } finally {
                // 记录发送结果
                mailMapper.insertMailTo(mailDto.getId(), to, status);
            }
        }
    }

    @Override
    public void send(MailDto mailDto, ToolMailConfig config) throws Exception {
        String toUsers = mailDto.getToUsers();
        if (StringUtils.isBlank(toUsers)) {
            throw new ServiceException("收件人不能为空");
        }
        toUsers = toUsers.replace(" ", "").replace("；", ";");
        String[] tos = toUsers.split(";");

        MailAccount account = new MailAccount();
        String user = config.getFromUser().split("@")[0];
        account.setUser(user);
        account.setHost(config.getHost());
        account.setPort(Integer.parseInt(config.getPort()));
        account.setAuth(true);
        account.setPass(config.getPass());
        account.setFrom(config.getUser() + "<" + config.getFromUser() + ">");
        account.setSslEnable(true); // SSL方式发送
        account.setStarttlsEnable(true); // STARTTLS安全连接

        for (String to : tos) {
            int status = 1;
            try {
                Mail.create(account)
                        .setTos(to)
                        .setTitle(mailDto.getSubject())
                        .setContent(mailDto.getContent())
                        .setHtml(true)
                        .setUseGlobalSession(false)
                        .send();
            } catch (Exception e) {
                status = 0;
                logger.debug("向 " + to + " 发送邮件失败！", e);
            } finally {
                mailMapper.insertMailTo(mailDto.getId(), to, status);
            }
        }
    }

    @Override
    public void sendSimple(MailDto mailDto, ToolMailConfig config) throws Exception {
        String toUsers = mailDto.getToUsers();
        if (StringUtils.isBlank(toUsers)) {
            throw new ServiceException("收件人不能为空");
        }
        toUsers = toUsers.replace(" ", "").replace("；", ";");
        String[] tos = toUsers.split(";");

        MailAccount account = new MailAccount();
        String user = config.getFromUser().split("@")[0];
        account.setUser(user);
        account.setHost(config.getHost());
        account.setPort(Integer.parseInt(config.getPort()));
        account.setAuth(true);
        account.setPass(config.getPass());
        account.setFrom(config.getUser() + "<" + config.getFromUser() + ">");
        account.setSslEnable(true); // SSL方式发送
        account.setStarttlsEnable(true); // STARTTLS安全连接

        Mail.create(account)
                .setTos(tos)
                .setTitle(mailDto.getSubject())
                .setContent(mailDto.getContent())
                .setHtml(true)
                .setUseGlobalSession(false)
                .send();
    }

    @Override
    @Transactional
    public MailDto addMail(MailDto mailDto) throws Exception {
        ToolMail mail = mailDto;
        mail.setUserId(SecurityUtil.getCurrentUserId());
        mailMapper.insertMail(mail);
        mailDto.setId(mail.getId()); // 记录主键
        return mailDto;
    }

    @Override
    public List<Map<String, Object>> findMailDetailsTo(Integer mailId) throws Exception {
        return mailMapper.selectMailDetailsTo(mailId);
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        // 邮件权限查询
        if (SecurityUtil.getCurrentUserPermission().contains("mail:self:query")) {
            request.getParams().put("userId", SecurityUtil.getCurrentUserId()); // 查询自身发送
        }

        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return mailMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    return mailMapper.selectAllMails(request.getParams(), request.getOffset(), request.getLimit());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }, new VueTableHandler.OrderHandler() {

            @Override
            public VueTableRequest order(VueTableRequest request) {
                Map<String, Object> params = request.getParams();
                if (null != params.get("orderBy")) {
                    String orderBy = params.get("orderBy").toString();
                    if (orderBy.contains("createTime")) {
                        params.put("orderBy", orderBy.replace("createTime", "create_time"));
                    }
                }
                return request;
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

}
