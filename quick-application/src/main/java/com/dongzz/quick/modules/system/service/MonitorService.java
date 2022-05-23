package com.dongzz.quick.modules.system.service;

import java.util.Map;

/**
 * 服务器监控相关接口
 *
 * @author zwk
 * @date 2022/5/18 11:08
 * @email zbcbbs@163.com
 */
public interface MonitorService {

    /**
     * 获取服务器信息
     *
     * @return
     * @throws Exception
     */
    Map<String, Object> getServerInfo() throws Exception;
}
