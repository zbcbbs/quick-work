package com.dongzz.quick.security.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 部门表
 */
@Setter
@Getter
public class SysDept implements Serializable {

    @Id
    private Integer id;

    private String name;

    private Integer pid;

    private Integer subCount;

    private Boolean enabled;

    private Integer sort;

    private Date createTime;

    private Date updateTime;

}
