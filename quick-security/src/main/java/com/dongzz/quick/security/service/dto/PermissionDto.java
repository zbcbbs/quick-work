package com.dongzz.quick.security.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class PermissionDto implements Serializable {

    private Integer id;

    private String type;

    private String permission;

    private String title; // 资源名称

    private Integer sort;

    private String href; // 链接地址

    private String component; // 组件

    private Integer parentId;

    private Integer subCount;

    private Boolean iFrame; // 是否外链

    private Boolean cache; // 缓存

    private Boolean hidden; // 隐藏

    private String name; // 组件名称

    private String icon; // 图标

    private Date createTime;

    private Date updateTime;

    private List<PermissionDto> children;

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    // 此字段在前端的 <treeselect/> 组件加载数据时起作用
    public String getLabel() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PermissionDto permissionDto = (PermissionDto) o;
        return Objects.equals(id, permissionDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
