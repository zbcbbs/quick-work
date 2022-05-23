package com.dongzz.quick.common.annotation.excel;

import java.lang.annotation.*;

/**
 * Excel 字段合并
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelGroup {

    /**
     * 多字段合并 分隔符
     */
    String value() default "";
}
