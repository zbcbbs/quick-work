package com.dongzz.quick.tools.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.tools.domain.ToolMail;
import com.dongzz.quick.tools.domain.ToolMailConfig;
import com.dongzz.quick.tools.service.dto.MailDto;

import java.util.List;
import java.util.Map;

/**
 * 邮件 相关服务接口
 */
public interface MailService extends BaseMybatisService<ToolMail> {

    /**
     * 保存配置
     *
     * @param config 配置
     * @return
     * @throws Exception
     */
    ToolMailConfig config(ToolMailConfig config) throws Exception;

    /**
     * 查询配置
     *
     * @return
     * @throws Exception
     */
    ToolMailConfig find() throws Exception;

    /**
     * 邮件发送，记录日志
     *
     * @param mailDto 已创建的
     * @throws Exception
     */
    void send(MailDto mailDto) throws Exception;

    /**
     * 邮件发送，记录日志
     *
     * @param mailDto 已创建的
     * @param config  邮箱配置
     * @throws Exception
     */
    void send(MailDto mailDto, ToolMailConfig config) throws Exception;

    /**
     * 邮件发送，不记录日志
     *
     * @param mailDto 邮件
     * @param config  邮箱配置
     * @throws Exception
     */
    void sendSimple(MailDto mailDto, ToolMailConfig config) throws Exception;


    /**
     * 添加邮件
     *
     * @param mailDto 邮件
     * @throws Exception
     */
    MailDto addMail(MailDto mailDto) throws Exception;

    /**
     * 查询邮件发送详情
     *
     * @param mailId 邮件编号
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findMailDetailsTo(Integer mailId) throws Exception;

    /**
     * 条件和分页查询
     *
     * @param request
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

}
