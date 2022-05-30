package com.dongzz.quick.security.service.impl;

import cn.hutool.core.map.MapUtil;
import com.dongzz.quick.security.dao.SysMemberMapper;
import com.dongzz.quick.security.dao.SysUserMapper;
import com.dongzz.quick.security.domain.SysMember;
import com.dongzz.quick.security.service.SecurityService;
import com.dongzz.quick.security.service.dto.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysMemberMapper memberMapper;

    @Override
    public Map<String, Object> getCurrentUser(LoginUser loginUser) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("id", loginUser.getId());
        data.put("username", loginUser.getUsername());
        data.put("status", loginUser.getStatus());
        data.put("resources", loginUser.getResources());
        data.put("isAdmin", loginUser.isAdmin());

        // 详细信息
        if (loginUser.isAdmin()) {
            Map<String, Object> user = userMapper.selectUserByUserid(loginUser.getId());
            Map<String, Object> detail = MapUtil.toCamelCaseMap(user); // 原始字段转化为驼峰命名
            data.put("detail", detail);
        } else {
            SysMember member = memberMapper.selectByPrimaryKey(loginUser.getId());
            data.put("detail", member);
        }

        return data;
    }

}
