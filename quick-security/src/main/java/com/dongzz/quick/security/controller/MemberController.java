package com.dongzz.quick.security.controller;

import com.dongzz.quick.common.utils.SecurityUtil;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.security.service.dto.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员
 */
@RestController
@RequestMapping("/api/members")
@Api(tags = "系统：会员管理", value = "会员管理相关接口")
public class MemberController {

    /**
     * 当前在线用户信息
     */
    @GetMapping("/current")
    @ApiOperation("当前在线用户")
    public ResponseVo current() throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtil.getCurrentUser();
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), loginUser);
    }
}
