package com.dongzz.quick.tools.config;

import com.dongzz.quick.common.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * 文件上传 配置
 */
@Configuration
public class MultipartConfig {

    public static final Logger logger = LoggerFactory.getLogger(MultipartConfig.class);

    /**
     * 上传临时目录
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = FileUtil.SYS_TEM_DIR;
        File file = new File(location);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                logger.debug("上传临时目录创建失败！");
            }
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
