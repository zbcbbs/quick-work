package com.dongzz.quick.security.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.security.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 数据访问接口
 */
public interface SysUserMapper extends BaseMybatisMapper<SysUser> {

    /**
     * 新增记录，获取主键
     *
     * @param user
     * @return
     * @throws Exception
     */
    int insertOne(SysUser user) throws Exception;

    /**
     * 用户授予角色
     *
     * @param userId  用户ID
     * @param roleIds 角色集合
     * @throws Exception
     */
    void insertUserRoles(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds)
            throws Exception;

    /**
     * 删除用户，逻辑删除
     *
     * @param userId 用户ID
     * @throws Exception
     */
    void deleteUser(Integer userId) throws Exception;

    /**
     * 修改用户状态
     *
     * @param id     用户ID
     * @param status 状态 启用，禁用，锁定
     * @throws Exception
     */
    void updateUserStatus(@Param("id") Integer id, @Param("status") String status) throws Exception;

    /**
     * 删除用户角色
     *
     * @param userId 用户ID
     * @throws Exception
     */
    void deleteUserRoles(Integer userId) throws Exception;

    /**
     * 根据用户名 查询用户
     *
     * @param username 用户名
     * @return
     * @throws Exception
     */
    SysUser selectUserByUsername(String username) throws Exception;

    /**
     * 根据手机号 查询用户
     *
     * @param phone 手机号码
     * @return
     * @throws Exception
     */
    SysUser selectUserByUphone(String phone) throws Exception;

    /**
     * 根据 邮箱 查询用户
     *
     * @param email 邮箱
     * @return
     * @throws Exception
     */
    SysUser selectUserByUemail(String email) throws Exception;


    /**
     * 根据ID 查询用户信息
     *
     * @param id ID
     * @return
     * @throws Exception
     */
    Map<String, Object> selectUserByUserid(Integer id) throws Exception;


    /**
     * 条件查询 统计数量
     *
     * @param params
     * @return
     * @throws Exception
     */
    Integer count(@Param("params") Map<String, Object> params) throws Exception;

    /**
     * 条件查询 分页
     *
     * @param params 查询条件 包含排序
     * @param offset 每页起始索引
     * @param limit  每页显示条数
     * @return
     * @throws Exception
     */
    List<LinkedHashMap> selectAllUsers(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
                                       @Param("limit") Integer limit) throws Exception;
}