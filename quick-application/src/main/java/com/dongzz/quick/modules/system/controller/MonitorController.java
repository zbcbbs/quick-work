package com.dongzz.quick.modules.system.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.modules.system.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 服务器监控
 *
 * @author zwk
 * @date 2022/5/18 11:06
 * @email zbcbbs@163.com
 */
@RestController
@RequestMapping("/api/monitor")
@Api(tags = "系统：服务器监控")
public class MonitorController extends BaseController {

    @Autowired
    private MonitorService monitorService;

    /**
     * 获取服务器信息
     */
    @GetMapping
    @ApiOperation(value = "获取服务器信息")
    public ResponseVo listServers() throws Exception {
        Map<String, Object> data = monitorService.getServerInfo();
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }
}
