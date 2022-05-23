package com.dongzz.quick.modules.quartz.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务表
 */
@Getter
@Setter
public class SysQuartzJob implements Serializable {

    public static final String JOB_KEY = "JOB_KEY"; // 任务信息存储标记

    @Id
    private Integer id;

    private String jobName;

    private String cron;

    private String springBeanName;

    private String methodName;

    private String params;

    // 系统任务 1：是 0：否
    private Boolean isSysJob;

    // 状态 1：运行  0：暂停
    private Boolean status;

    private String personInCharge; // 负责人

    private Boolean pauseAfterFailure; // 任务失败后是否暂停

    private String email; // 报警邮箱

    private String subTask; // 子任务ID

    private String description;

    private Date createTime;

    private Date updateTime;

    @Transient
    private String uuid;


}