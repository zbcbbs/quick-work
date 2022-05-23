package com.dongzz.quick.generator.domain;

import com.dongzz.quick.generator.utils.GenUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 字段信息存储表
 */
@Getter
@Setter
@NoArgsConstructor
public class CodeColumnInfo implements Serializable {

    @Id
    private Long id;
    private String tableName;
    private String columnName;
    private String columnType;
    private String keyType;
    private String extra;
    private String remark;
    private Boolean notNull;
    private Boolean listShow;
    private Boolean formShow;
    private String formType;
    private String queryType;
    private String dictName;
    private String dateAnnotation;

    public CodeColumnInfo(String tableName, String columnName, Boolean notNull, String columnType, String remark, String keyType, String extra) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnType = columnType;
        this.keyType = keyType;
        this.extra = extra;
        this.notNull = notNull;
        if (GenUtil.PK.equalsIgnoreCase(keyType) && GenUtil.EXTRA.equalsIgnoreCase(extra)) {
            this.notNull = false;
        }
        this.remark = remark;
        this.listShow = true;
        this.formShow = true;
    }
}
