package com.dongzz.quick.common.annotation.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询
 *
 * @author zwk
 * @date 2022/5/12 18:35
 * @email zbcbbs@163.com
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /**
     * 属性名称
     */
    String prop() default "";

    /**
     * 查询类型
     */
    Type type() default Type.EQ;

    enum Type {
        EQ // 等于
        , GE // 大于等于
        , GT // 大于
        , LE // 小于等于
        , LT // 小于
        , LIKE // 模糊查询
        , STARTWITH // 左模糊匹配
        , ENDWITH // 右模糊匹配
        , IN // 包含
        , NOT_IN // 不包含
        , NOT_EQ // 不等于
        , BETWEEN // 区间
        , NOT_NULL // 非空
        , IS_NULL // 空
    }

}
