package com.dongzz.quick.common.config.bean;

import com.dongzz.quick.common.utils.Constants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传 参数配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    private Long maxSize; // 文件大小限制
    private Long avatarMaxSize; // 头像大小限制
    private String domain; // 访问域名

    private ElPath mac;
    private ElPath linux;
    private ElPath windows;
    private ElMapping mapping;

    /**
     * 根据操作系统 获取上传路径
     *
     * @return
     */
    public ElPath getPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(Constants.WIN)) {
            return windows;
        } else if (os.toLowerCase().startsWith(Constants.MAC)) {
            return mac;
        }
        return linux;
    }

    /**
     * 获取 MVC 静态资源访问映射
     *
     * @return
     */
    public ElMapping getMapping() {
        return mapping;
    }


    /**
     * 上传路径
     */
    @Data
    public static class ElPath {
        private String path; // 文件上传
        private String avatar; // 头像上传
    }

    /**
     * MVC 静态资源访问映射
     */
    @Data
    public static class ElMapping {
        private String path;
        private String avatar;
    }
}
