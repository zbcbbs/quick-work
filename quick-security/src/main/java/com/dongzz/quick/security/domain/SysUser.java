package com.dongzz.quick.security.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 */
@Getter
@Setter
public class SysUser implements Serializable {

    @Id
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private Integer deptId; // 部门

    private String headImgUrl;

    private String phone;

    private String telephone;

    private String email;

    // 日期格式化
    @JSONField(format = "yyyy-MM-dd")
    private Date birthday;

    private String remark;

    private String sex;

    private String status; // 状态

    private Date createTime;

    private Date updateTime;

    private String lastIp;

    private Date lastTime;

    private int count; // 登录次数

    private String isDel;

    /**
     * 账号状态
     */
    public interface Status {
        // 禁用
        String DISABLED = "0";
        // 正常
        String VALID = "1";
        // 锁定
        String LOCKED = "2";
    }
}