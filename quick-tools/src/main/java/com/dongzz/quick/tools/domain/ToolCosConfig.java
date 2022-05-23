package com.dongzz.quick.tools.domain;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 腾讯云上传配置
 */
public class ToolCosConfig implements Serializable {

    @Id
    private Integer id;
    private String bucket;
    private String secretId;
    private String secretKey;
    private String appId;
    private String domain;
    private String region;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 获取桶名称
     *
     * @return
     */
    public String bucket() {
        return bucket + "-" + appId;
    }
}
