package com.dongzz.quick.security.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.*;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.security.domain.SysUser;
import com.dongzz.quick.security.service.UserService;
import com.dongzz.quick.security.service.dto.EmailDto;
import com.dongzz.quick.security.service.dto.LoginUser;
import com.dongzz.quick.security.service.dto.PassDto;
import com.dongzz.quick.security.service.dto.UserDto;
import com.dongzz.quick.tools.domain.ToolCosConfig;
import com.dongzz.quick.tools.domain.ToolCosFile;
import com.dongzz.quick.tools.service.FileCosService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 用户
 */
@RestController
@RequestMapping("/api/users")
@Api(tags = "系统：用户管理", value = "用户管理相关接口")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileCosService fileService;

    /**
     * 新增
     */
    @PostMapping
    @ApiOperation(value = "新增用户", notes = "新增用户")
    public ResponseVo add(@ApiParam(name = "用户实体", value = "参数体", required = true) @RequestBody UserDto userDto) throws Exception {
        // 新增用户
        userService.addUser(userDto);
        ResponseVo responseVO = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return responseVO;
    }

    /**
     * 删除，逻辑，支持批量
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户", notes = "删除用户，逻辑，支持批量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID 如：1 或 1,2", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable("id") String id) throws Exception {
        userService.deleteUser(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改
     */
    @PutMapping
    @ApiOperation(value = "修改用户", notes = "修改用户")
    public ResponseVo update(@ApiParam(name = "用户实体", value = "参数体", required = true) @RequestBody UserDto userDto) throws Exception {
        // 修改用户
        userService.updateUser(userDto);
        ResponseVo responseVO = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return responseVO;
    }

    /**
     * 修改指定用户状态
     */
    @PutMapping("/updateStatus")
    @ApiOperation(value = "修改用户状态", notes = "修改用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "用户状态 0,1,2", dataType = "string", paramType = "query", required = true)
    })
    public ResponseVo update(Integer id, String status) throws Exception {
        userService.updateStatus(id, status);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改个人邮箱
     */
    @PostMapping("/updateEmail")
    @ApiOperation("修改邮箱")
    public ResponseVo update(@RequestBody EmailDto emailDto) throws Exception {
        userService.updateEmail(emailDto);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }


    /**
     * 修改个人密码
     */
    @PostMapping("/updatePass")
    @ApiOperation("修改密码")
    public ResponseVo update(@RequestBody PassDto passDto) throws Exception {
        userService.updatePass(passDto);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改上传头像
     */
    @PostMapping("/updateAvatar")
    @ApiOperation("修改上传头像")
    public ResponseVo update(@RequestParam("avatar") MultipartFile file) throws Exception {
        ToolCosConfig config = fileService.find();
        ToolCosFile tcf = fileService.addFile(file, config);
        SysUser user = new SysUser();
        user.setId(SecurityUtil.getCurrentUserId());
        user.setHeadImgUrl(tcf.getUrl());
        userService.updateSelective(user);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }


    /**
     * 获取 用户列表
     */
    @GetMapping
    @ApiOperation(value = "获取用户列表", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query", required = true)
    })
    public VueTableResponse listUsers(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = userService.findAll(request);
        return response;
    }

    /**
     * 获取 用户详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取用户详情", notes = "获取用户详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "integer", paramType = "path", required = true)
    })
    public ResponseVo listUser(@PathVariable("id") Integer id) throws Exception {
        SysUser user = userService.selectByPk(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), user);
    }


    /**
     * 导出 Excel
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出用户", notes = "导出用户")
    public void export(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse vtp = userService.findAll(request);
        List<UserDto> userDtos = (List<UserDto>) vtp.getData();
        userService.download(userDtos, response);
    }

    /**
     * 获取 当前 在线用户信息
     */
    @GetMapping("/current")
    @ApiOperation(value = "当前在线用户", notes = "当前在线用户")
    public ResponseVo current() throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtil.getCurrentUser();
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), loginUser);
    }

}
