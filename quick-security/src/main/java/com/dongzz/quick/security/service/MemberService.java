package com.dongzz.quick.security.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.security.domain.SysMember;

/**
 * 会员 相关服务接口
 */
public interface MemberService extends BaseMybatisService<SysMember> {

    /**
     * 根据 用户名 查询会员
     *
     * @param username 用户名
     * @return
     * @throws Exception
     */
    SysMember findMemberByUname(String username) throws Exception;


    /**
     * 根据 邮箱 查询会员
     *
     * @param email 邮箱
     * @return
     * @throws Exception
     */
    SysMember findMemberByUemail(String email) throws Exception;

    /**
     * 根据 手机号 查询会员
     *
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    SysMember findMemberByUphone(String phone) throws Exception;
}
