package com.dongzz.quick.generator.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 代码生成配置表
 */
@Getter
@Setter
@NoArgsConstructor
public class CodeGenConfig implements Serializable {

    @Id
    private Long id;
    private String tableName;
    private String apiAlias;
    private String pack;
    private String moduleName;
    private String path;
    private String apiPath;
    private String author;
    private String email;
    private String comment;
    private String prefix;
    private Boolean cover;

    public CodeGenConfig(String tableName) {
        this.tableName = tableName;
    }
}
