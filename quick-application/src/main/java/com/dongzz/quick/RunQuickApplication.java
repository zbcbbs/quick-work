package com.dongzz.quick;

import com.dongzz.quick.common.utils.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 启动类 应用入口
 */
@ApiIgnore
@Controller
@SpringBootApplication
@EnableTransactionManagement // 事务管理
public class RunQuickApplication {

    // 程序入口
    public static void main(String[] args) {
        SpringApplication.run(RunQuickApplication.class);
    }

    /**
     * 自定义 Tomcat 作为内嵌服务器
     */
    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory wsf = new TomcatServletWebServerFactory();
        wsf.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return wsf;
    }

    /**
     * 初始化 spring 上下文工具
     */
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    /**
     * 首页
     */
    @RequestMapping("/")
    public String index() {
        return "index"; // index.ftl
    }
}
