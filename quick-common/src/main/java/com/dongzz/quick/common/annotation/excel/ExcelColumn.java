package com.dongzz.quick.common.annotation.excel;

import java.lang.annotation.*;

/**
 * Excel 字段
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

    /**
     * 列标题
     */
    String value() default "";

    /**
     * 列位置 由左向右
     */
    int col() default 0;

}
