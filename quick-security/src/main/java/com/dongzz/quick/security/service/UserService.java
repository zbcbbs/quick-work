package com.dongzz.quick.security.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.security.domain.SysUser;
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
     * @param user
     * @throws Exception
     */
    void addUser(SysUser user) throws Exception;

    /**
     * 新增用户
     *
     * @param userDto 用户拓展对象
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
     * 用户授予角色
     *
     * @param userId  用户ID
     * @param roleIds 角色数组
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
     * 修改用户状态
     *
     * @param id     用户ID
     * @param status 状态
     * @throws Exception
     */
    void updateUser(Integer id, String status) throws Exception;

    /**
     * 分页 条件查询
     *
     * @param request 分页请求
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

    /**
     * 根据 用户名 查询用户
     *
     * @param username 用户名
     * @return
     * @throws Exception
     */
    SysUser findUserByUname(String username) throws Exception;


    /**
     * 根据 邮箱 查询用户
     *
     * @param email 邮箱
     * @return
     * @throws Exception
     */
    SysUser findUserByUemail(String email) throws Exception;

    /**
     * 根据 手机号 查询用户
     *
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    SysUser findUserByUphone(String phone) throws Exception;


    /**
     * 下载导出 Excel
     *
     * @param userDtos 数据
     * @param response
     * @throws Exception
     */
    void download(List<UserDto> userDtos, HttpServletResponse response) throws Exception;
}
