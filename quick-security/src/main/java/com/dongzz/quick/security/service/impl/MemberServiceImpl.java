package com.dongzz.quick.security.service.impl;

import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.security.dao.SysMemberMapper;
import com.dongzz.quick.security.domain.SysMember;
import com.dongzz.quick.security.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberServiceImpl extends BaseMybatisServiceImpl<SysMember> implements MemberService {

    @Autowired
    private SysMemberMapper memberMapper;

    @Override
    public SysMember findMemberByUname(String username) throws Exception {
        return memberMapper.selectMemberByUsername(username);
    }

    @Override
    public SysMember findMemberByUemail(String email) throws Exception {
        return memberMapper.selectMemberByUemail(email);
    }

    @Override
    public SysMember findMemberByUphone(String phone) throws Exception {
        return memberMapper.selectMemberByUphone(phone);
    }
}
