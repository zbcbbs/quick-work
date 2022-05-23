package com.dongzz.quick.logging.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务 日志表
 */
@Getter
@Setter
public class SysQuartzLog implements Serializable {

    @Id
    private Long id;

    private String jobName;

    private String beanName;

    private String methodName;

    private String params;

    private String cron;

    private Long time; // 执行耗时

    private Boolean isSuccess; // 状态

    private String exception; // 异常信息

    private Date createTime;
}
