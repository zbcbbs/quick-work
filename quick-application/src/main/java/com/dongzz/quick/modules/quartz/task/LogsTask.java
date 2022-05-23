package com.dongzz.quick.modules.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.dongzz.quick.common.annotation.schedule.Scheduled;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.logging.domain.SysLogs;
import com.dongzz.quick.logging.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 日志相关的定时任务
 *
 * @author zwk
 * @date 2022/5/19 14:35
 * @email zbcbbs@163.com
 */
@Scheduled
@Component
public class LogsTask {

    @Autowired
    private LogsService logsService;

    /**
     * 删除7天前的日志
     *
     * @throws Exception
     */
    @Scheduled
    public void deleteLogs() throws Exception {
        // 删除7天前的日志
        DateTime offetTime = DateUtil.offsetDay(new Date(), -7);
        String enddate = offetTime.toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> params = new HashMap<>();
        params.put("enddate", enddate);
        VueTableRequest request = new VueTableRequest();
        request.setParams(params);
        VueTableResponse vtr = logsService.findAll(request);
        List<?> data = vtr.getData();
        if (CollectionUtil.isNotEmpty(data)) {
            List<SysLogs> logs = (List<SysLogs>) data;
            List<Long> logIds = logs.stream().filter(log -> log.getId() != null).map(SysLogs::getId).collect(Collectors.toList());
            String ids = StringUtil.join(logIds, ",");
            logsService.deleteLogs(ids);
        }
        System.out.println("==== 删除日志，共删除了 " + data.size() + " 条日志！");
    }

}
