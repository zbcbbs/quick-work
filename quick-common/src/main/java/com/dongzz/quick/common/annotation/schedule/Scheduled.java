package com.dongzz.quick.common.annotation.schedule;

import java.lang.annotation.*;

/**
 * 定时任务
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled {

}
