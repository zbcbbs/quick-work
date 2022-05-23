package com.dongzz.quick.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流
 *
 * @author zwk
 * @date 2022/5/17 10:03
 * @email zbcbbs@163.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

    // 接口描述
    String name() default "";

    // 资源 key
    String key() default "";

    // key prefix
    String prefix() default "";

    // 时间段，单位秒
    int period();

    // 限制访问数量
    int count();

    // 限流类型
    LimitType limitType() default LimitType.CUSTOMER;

    /**
     * 限流类型
     */
    enum LimitType {
        // 默认
        CUSTOMER,
        // ip
        IP
    }
}
