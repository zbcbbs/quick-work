package com.dongzz.quick.logging.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.logging.domain.SysQuartzLog;

/**
 * 定时任务日志 服务接口
 */
public interface QuartzLogService extends BaseMybatisService<SysQuartzLog> {

    /**
     * 分页 条件查询
     *
     * @param request
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;
}
