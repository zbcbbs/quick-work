package com.dongzz.quick.common.annotation.excel;

import java.lang.annotation.*;

/**
 * Excel 日期字段
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelDate {

    /**
     * 日期格式
     */
    String value() default "yyyy-MM-dd HH:mm:ss";
}
