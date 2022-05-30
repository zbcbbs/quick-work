package com.dongzz.quick.security.service;

import com.dongzz.quick.security.service.dto.LoginUser;

import java.util.Map;

/**
 * 系统安全相关服务接口
 *
 * @author zwk
 * @date 2022/5/28 16:53
 * @email zbcbbs@163.com
 */
public interface SecurityService {


    /**
     * 封装登录用户的信息 响应到前端
     *
     * @param loginUser 登录用户
     * @return
     * @throws Exception
     */
    Map<String, Object> getCurrentUser(LoginUser loginUser) throws Exception;


}
