package com.dongzz.quick.security.controller;

import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.EncryptUtil;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.logging.annotation.Log;
import com.dongzz.quick.security.service.OnlineUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 在线用户
 */
@RestController
@RequestMapping("/auth/online")
@Api(tags = "系统：在线用户管理")
public class OnlineController {

    @Autowired
    private OnlineUserService onlineUserService;

    /**
     * 查询在线用户列表
     */
    @GetMapping
    @ApiOperation("查询在线用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listUsers(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = onlineUserService.getAll(request);
        return response;
    }

    /**
     * 踢出用户
     */
    @DeleteMapping
    @ApiOperation("踢出用户")
    @Log(module = "系统", operate = "强制下线")
    public ResponseVo delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            // 令牌解密
            key = EncryptUtil.desDecrypt(key);
            onlineUserService.kickOut(key);
        }
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 导出在线用户数据
     */
    @GetMapping(value = "/export")
    @ApiOperation("导出数据")
    public void export(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.download(onlineUserService.getAll(filter), response);
    }


}
