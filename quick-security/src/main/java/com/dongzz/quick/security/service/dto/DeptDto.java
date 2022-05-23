package com.dongzz.quick.security.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class DeptDto implements Serializable {

    private Integer id;

    private String name;

    private Integer pid;

    private Integer subCount;

    private Boolean enabled;

    private Integer sort;

    private Date createTime;

    private Date updateTime;

    List<DeptDto> children; // 子部门


    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return name;
    }

}
