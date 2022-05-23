package com.dongzz.quick.logging.service.impl;

import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.logging.dao.SysQuartzLogMapper;
import com.dongzz.quick.logging.domain.SysQuartzLog;
import com.dongzz.quick.logging.service.QuartzLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuartzLogServiceImpl extends BaseMybatisServiceImpl<SysQuartzLog> implements QuartzLogService {

    @Autowired
    private SysQuartzLogMapper quartzLogMapper;

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {

        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {
            @Override
            public int count(VueTableRequest request) {
                try {
                    return quartzLogMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {
            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    return quartzLogMapper.selectAllQuartzLogs(request.getParams(), request.getOffset(), request.getLimit());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }
}
