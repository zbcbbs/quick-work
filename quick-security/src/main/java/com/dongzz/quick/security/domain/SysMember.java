package com.dongzz.quick.security.domain;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员表
 */
@Data
public class SysMember implements Serializable {

    @Id
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String headImgUrl;

    private String status; // 账号状态

    private String sex;

    private Date birthday;

    private String email;

    private String telephone;

    private String mobile;

    private String qq;

    private String wechat;

    private String alipay;

    private BigDecimal balance;

    private Double score;

    private Date createTime;

    private Date updateTime;

    private Date lastTime;

    private String lastIp;

    private String isDel;

    /**
     * 账户状态
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