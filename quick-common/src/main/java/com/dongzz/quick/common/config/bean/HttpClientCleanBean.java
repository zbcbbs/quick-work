package com.dongzz.quick.common.config.bean;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 自定义线程 定时清理 Http Client 过期连接和无效连接
 * 定时运行 closeExpiredConnections() 和 closeIdleConnections() 方法 清理过期和空闲的连接
 */
public class HttpClientCleanBean extends Thread {

    public static final Logger logger = LoggerFactory.getLogger(HttpClientCleanBean.class);

    private HttpClientConnectionManager connectionManager; // 连接池管理器
    private volatile boolean shutdown; // 线程停止标记 volatile修饰 多线程间可见

    public HttpClientCleanBean(HttpClientConnectionManager connectionManager) {
        super();
        this.connectionManager = connectionManager;
        logger.debug("Start up Http Client clean thread.");
    }


    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000); // 每 5s 循环
                    // Close expired connections
                    connectionManager.closeExpiredConnections(); // 清理过期连接
                    // Optionally, close connections
                    // that have been idle longer than 30 sec
                    connectionManager.closeIdleConnections(30, TimeUnit.SECONDS); // 清理空闲 30s 以上的连接
                    logger.debug("Execute Http Client connection clean.");
                }
            }
        } catch (InterruptedException ex) {
            shutdown(); // 抛出异常 停止线程执行
        }
    }

    /**
     * 更改变量 shutdown 的值，停止线程执行
     */
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
