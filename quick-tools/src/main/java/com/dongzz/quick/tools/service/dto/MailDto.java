package com.dongzz.quick.tools.service.dto;

import com.dongzz.quick.tools.domain.ToolMail;
import lombok.Getter;
import lombok.Setter;

/**
 * 邮件实体扩展
 */
@Getter
@Setter
public class MailDto extends ToolMail {

    /**
     * 收件人邮箱，支持多个
     */
    private String toUsers;
}
