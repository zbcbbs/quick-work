package com.dongzz.quick.logging.annotation;

import java.lang.annotation.*;

/**
 * 日志
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 模块
     */
    String module() default "";

    /**
     * 操作内容
     */
    String operate() default "";

}
