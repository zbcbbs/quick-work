package com.dongzz.quick.security.authentication;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 前台提交的 额外认证信息
 */
@Setter
@Getter
public class DefaultAuthenticationDetails implements Serializable {

    private HttpServletRequest request;

    private String code; // 验证码
    private String uuid; // 验证码缓存

    // 请求参数名称
    private String codeParameter = "code";
    private String uuidParameter = "uuid";

    /**
     * 获取 前台请求中的 认证信息
     *
     * @param request 请求
     */
    public DefaultAuthenticationDetails(HttpServletRequest request) {
        this.request = request;
        this.code = StringUtils.isNotBlank(request.getParameter(this.codeParameter)) ? request.getParameter(this.codeParameter) : "";
        this.uuid = StringUtils.isNotBlank(request.getParameter(this.uuidParameter)) ? request.getParameter(this.uuidParameter) : "";
    }

}
