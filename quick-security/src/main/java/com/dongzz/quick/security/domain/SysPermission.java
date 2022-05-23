package com.dongzz.quick.security.domain;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 资源 权限表
 */
@Data
public class SysPermission implements Serializable {

    @Id
    private Integer id;

    private Integer parentId; // 上级资源

    private Integer subCount; // 子资源数目

    private String type; // 资源类型

    private String title; // 资源名称

    private String name; // 组件名称

    private String component; // 组件

    private Integer sort; // 排序

    private String icon; // 图标

    private String href; // 链接地址

    private Boolean iFrame; // 是否外链

    private Boolean cache; // 缓存

    private Boolean hidden; // 隐藏

    private String permission; // 权限码

    private String source; // 系统标记

    private Date createTime; // 创建时间

    private Date updateTime; // 修改时间

    private String isDel; // 逻辑删除

}