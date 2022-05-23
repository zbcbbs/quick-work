package com.dongzz.quick.security.domain;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 令牌表
 */
public class SysToken implements Serializable {

    @Id
    private String id;
    private Date expireTime; // 过期时间
    private Date createTime; // 创建时间
    private Date updateTime; // 修改时间
    private String val; // 在线用户json存储

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}