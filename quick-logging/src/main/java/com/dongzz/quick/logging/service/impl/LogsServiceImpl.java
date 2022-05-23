package com.dongzz.quick.logging.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.ExcelUtil;
import com.dongzz.quick.common.utils.Util;
import com.dongzz.quick.logging.dao.SysLogsMapper;
import com.dongzz.quick.logging.service.LogsService;
import com.dongzz.quick.logging.domain.SysLogs;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class LogsServiceImpl extends BaseMybatisServiceImpl<SysLogs> implements LogsService {

    @Autowired
    private SysLogsMapper logsMapper;

    @Override
    @Transactional
    public void addLogs(String user, String module, String content, String status) throws Exception {
        SysLogs logs = new SysLogs();
        logs.setUser(user);
        logs.setModule(module);
        logs.setContent(content);
        logs.setStatus(status);
        logs.setTime(new Date());
        logsMapper.insertSelective(logs);
    }

    @Override
    @Transactional
    public void deleteLogs(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            // 判断 是否批量删除
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String lid : ids) {
                    // 逻辑删除
                    logsMapper.deleteLogs(Long.parseLong(lid));
                }
            } else {
                logsMapper.deleteLogs(Long.parseLong(id));
            }
        }
    }

    @Override
    @Transactional
    public void deleteLogs(VueTableRequest request) throws Exception {
        request.setLimit(null); // 消除分页
        VueTableResponse vtr = this.findAll(request);
        List<SysLogs> data = (List<SysLogs>) vtr.getData();
        if (CollectionUtil.isNotEmpty(data)) {
            for (SysLogs log : data) {
                logsMapper.deleteLogs(log.getId());
            }
        }
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return logsMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    return logsMapper.selectAllLogs(request.getParams(), request.getOffset(), request.getLimit());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

    @Override
    public void download(VueTableRequest request, HttpServletResponse response) throws Exception {
        request.setLimit(null); // 消除分页
        VueTableResponse vtr = this.findAll(request);
        List<SysLogs> data = (List<SysLogs>) vtr.getData();
        if (CollectionUtil.isNotEmpty(data)) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (SysLogs log : data) {
                Map<String, Object> map = new LinkedHashMap<>(); // 此处使用 LinkedHashMap，否则顺序混乱
                map.put("ID", log.getId());
                map.put("用户", log.getUser());
                map.put("模块", log.getModule());
                map.put("描述", log.getContent());
                map.put("状态", "1".equals(log.getStatus()) ? "成功" : "异常");
                map.put("位置", log.getClazz());
                map.put("方法", log.getMethod());
                map.put("参数", log.getParam());
                map.put("IP", log.getIp());
                map.put("url", log.getUrl());
                map.put("时间", com.dongzz.quick.common.utils.DateUtil.format(log.getTime(), "yyyy-MM-dd HH:mm:ss"));
                map.put("耗时/ms", log.getTotalTime());
                list.add(map);
            }
            ExcelUtil.writeExcel(list, response);
        }
    }

}
