package com.dongzz.quick.security.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 前端路由
 */
@Data
public class MenuVo implements Serializable {

    private String name;

    private String path;

    private Boolean hidden;

    private String redirect;

    private String component;

    private Boolean alwaysShow;

    private MenuMetaVo meta; // 元数据

    private List<MenuVo> children; // 子路由
}
