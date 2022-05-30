package com.dongzz.quick.security.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.security.domain.SysUser;
import com.dongzz.quick.security.service.dto.EmailDto;
import com.dongzz.quick.security.service.dto.PassDto;
import com.dongzz.quick.security.service.dto.UserDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户 相关服务接口
 */
public interface UserService extends BaseMybatisService<SysUser> {

    /**
     * 新增用户
     *
     * @param user 用户
     * @throws Exception
     */
    void addUser(SysUser user) throws Exception;

    /**
     * 新增用户
     *
     * @param userDto 用户
     * @throws Exception
     */
    void addUser(UserDto userDto) throws Exception;

    /**
     * 修改用户
     *
     * @param userDto
     * @throws Exception
     */
    void updateUser(UserDto userDto) throws Exception;

    /**
     * 修改指定用户的状态
     *
     * @param id     ID
     * @param status 状态
     * @throws Exception
     */
    void updateStatus(Integer id, String status) throws Exception;

    /**
     * 修改个人邮箱
     *
     * @param emailDto 邮箱
     * @throws Exception
     */
    void updateEmail(EmailDto emailDto) throws Exception;

    /**
     * 修改个人密码
     *
     * @param passDto 密码
     * @throws Exception
     */
    void updatePass(PassDto passDto) throws Exception;

    /**
     * 用户授予角色
     *
     * @param userId  ID
     * @param roleIds 角色集合
     * @throws Exception
     */
    void grantRoles(Integer userId, List<Integer> roleIds) throws Exception;

    /**
     * 删除用户，支持批量
     *
     * @param id 用户编号 1 或 1,2
     * @throws Exception
     */
    void deleteUser(String id) throws Exception;

    /**
     * 分页 条件查询
     *
     * @param request 分页请求
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return
     * @throws Exception
     */
    SysUser findUserByUname(String username) throws Exception;


    /**
     * 根据邮箱查询
     *
     * @param email 邮箱
     * @return
     * @throws Exception
     */
    SysUser findUserByUemail(String email) throws Exception;

    /**
     * 根据手机号查询
     *
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    SysUser findUserByUphone(String phone) throws Exception;

    /**
     * 下载导出
     *
     * @param userDtos 数据
     * @param response
     * @throws Exception
     */
    void download(List<UserDto> userDtos, HttpServletResponse response) throws Exception;
}
