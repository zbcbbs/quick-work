package com.dongzz.quick.tools.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.tools.service.CodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码
 *
 * @author zwk
 * @date 2022/5/28 20:31
 * @email zbcbbs@163.com
 */
@RestController
@RequestMapping("/api/code")
@Api(tags = "工具：验证码", value = "验证码接口")
public class CodeController extends BaseController {

    @Autowired
    private CodeService codeService;

    /**
     * 邮箱验证码
     */
    @PostMapping("/email")
    @ApiOperation("邮箱验证码")
    public ResponseVo email(@RequestParam String email) throws Exception {
        codeService.sendEmailCode(email);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }
}
