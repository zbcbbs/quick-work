package com.dongzz.quick.logging.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.logging.domain.SysLogs;

import javax.servlet.http.HttpServletResponse;

/**
 * 系统日志 服务接口
 */
public interface LogsService extends BaseMybatisService<SysLogs> {

    /**
     * 新增日志
     *
     * @param user    操作人
     * @param module  模块
     * @param content 内容
     * @param status  状态 '1':成功 '0':异常
     * @throws Exception
     */
    void addLogs(String user, String module, String content, String status) throws Exception;

    /**
     * 删除，逻辑，支持批量
     *
     * @param id 日志ID 1 或 1,2
     * @throws Exception
     */
    void deleteLogs(String id) throws Exception;

    /**
     * 批量清空日志
     *
     * @throws Exception
     */
    void deleteLogs(VueTableRequest request) throws Exception;

    /**
     * 分页 条件查询
     *
     * @param request 分页请求
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

    /**
     * 导出数据
     *
     * @param response
     * @throws Exception
     */
    void download(VueTableRequest request, HttpServletResponse response) throws Exception;
}
