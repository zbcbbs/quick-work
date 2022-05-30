package com.dongzz.quick.security.service;

import cn.hutool.core.util.ObjectUtil;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.*;
import com.dongzz.quick.security.config.bean.JwtProperties;
import com.dongzz.quick.security.service.dto.LoginUser;
import com.dongzz.quick.security.service.dto.OnlineUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 在线用户 服务接口
 */
@Service
public class OnlineUserService {

    public static final Logger logger = LoggerFactory.getLogger(OnlineUserService.class);

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 保存 在线用户信息
     *
     * @param loginUser /
     * @param request   /
     */
    public void save(LoginUser loginUser, String token, HttpServletRequest request) {
        String ip = StringUtil.getIp(request);
        String browser = StringUtil.getBrowser(request);
        String address = StringUtil.getCityInfo(ip);

        OnlineUser onlineUser = null;
        try {
            onlineUser = new OnlineUser(loginUser, loginUser.getUsername(), browser, ip, address, EncryptUtil.desEncrypt(token), new Date());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        // key = online-token- + token
        redisUtil.set(jwtProperties.getOnlineKey() + token, onlineUser, jwtProperties.getTokenExpireTime() / 1000);
    }

    /**
     * 查询 全部数据
     *
     * @param request /
     * @return /
     */
    public VueTableResponse getAll(VueTableRequest request) {
        String filter = ObjectUtil.isNotNull(request.getParams().get("filter")) ? request.getParams().get("filter").toString() : "";
        List<OnlineUser> onlineUsers = getAll(filter);
        Map<String, Object> page = PageUtil.toPage(
                PageUtil.toPage(request.getOffset(), request.getLimit(), onlineUsers),
                onlineUsers.size()
        );
        List<OnlineUser> data = (List<OnlineUser>) page.get("content"); // 当前页记录
        Integer total = (Integer) page.get("totalElements"); // 数量
        return new VueTableResponse(total, total, data);

    }

    /**
     * 查询全部数据，不分页
     *
     * @param filter /
     * @return /
     */
    public List<OnlineUser> getAll(String filter) {
        List<String> keys = redisUtil.scan(jwtProperties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUser onlineUser = (OnlineUser) redisUtil.get(key); // 在线用户
            if (StringUtil.isNotBlank(filter)) {
                if (onlineUser.toString().contains(filter)) {
                    onlineUsers.add(onlineUser);
                }
            } else {
                onlineUsers.add(onlineUser);
            }
        }
        onlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUsers;
    }

    /**
     * 踢出用户
     *
     * @param token 令牌
     */
    public void kickOut(String token) {
        String key = jwtProperties.getOnlineKey() + token;
        redisUtil.del(key);
    }

    /**
     * 退出登录
     *
     * @param token 令牌
     */
    public void logout(String token) {
        String key = jwtProperties.getOnlineKey() + token;
        redisUtil.del(key);
    }

    /**
     * 导出
     *
     * @param all      /
     * @param response /
     * @throws IOException /
     */
    public void download(List<OnlineUser> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUser user : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("账号", user.getUserName());
            map.put("IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", DateUtil.format(user.getLoginTime(), "yyyy-MM-dd HH:mm:ss"));
            list.add(map);
        }
        ExcelUtil.writeExcel(list, response);
    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public OnlineUser getOne(String key) {
        return (OnlineUser) redisUtil.get(key);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken) {
        List<OnlineUser> onlineUsers = getAll(userName);
        if (onlineUsers == null || onlineUsers.isEmpty()) {
            return;
        }
        for (OnlineUser onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(userName)) {
                try {
                    // 获取令牌 进行解密
                    String token = EncryptUtil.desDecrypt(onlineUser.getToken());
                    if (StringUtil.isNotBlank(igoreToken) && !igoreToken.equals(token)) {
                        this.kickOut(token);
                    } else if (StringUtil.isBlank(igoreToken)) {
                        this.kickOut(token);
                    }
                } catch (Exception e) {
                    logger.error("checkUser is error", e);
                }
            }
        }
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username) throws Exception {
        List<OnlineUser> onlineUsers = getAll(username);
        for (OnlineUser onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(username)) {
                String token = EncryptUtil.desDecrypt(onlineUser.getToken());
                kickOut(token);
            }
        }
    }

}
