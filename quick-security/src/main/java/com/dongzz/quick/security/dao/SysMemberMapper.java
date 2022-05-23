package com.dongzz.quick.security.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.security.domain.SysMember;

/**
 * 会员 数据访问接口
 */
public interface SysMemberMapper extends BaseMybatisMapper<SysMember> {

    /**
     * 根据用户名 查询会员
     *
     * @param username 用户名
     * @return
     * @throws Exception
     */
    SysMember selectMemberByUsername(String username) throws Exception;

    /**
     * 根据手机号 查询会员
     *
     * @param phone 手机号码
     * @return
     * @throws Exception
     */
    SysMember selectMemberByUphone(String phone) throws Exception;

    /**
     * 根据 邮箱 查询会员
     *
     * @param email 邮箱
     * @return
     * @throws Exception
     */
    SysMember selectMemberByUemail(String email) throws Exception;
}