package com.dongzz.quick.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * 验证码异常
 */
@Getter
public class VerificationException extends AuthenticationException {

    private Integer status = UNAUTHORIZED.value();

    public VerificationException(String msg) {
        super(msg);
    }

    public VerificationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public VerificationException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }

    public VerificationException(HttpStatus status, String msg, Throwable cause) {
        super(msg, cause);
        this.status = status.value();
    }

}
