package com.dongzz.quick.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 认证异常
 */
@Getter
public class AuthenticationException extends RuntimeException {

    private Integer status = BAD_REQUEST.value();

    public AuthenticationException(String msg) {
        super(msg);
    }

    public AuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AuthenticationException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }

    public AuthenticationException(HttpStatus status, String msg, Throwable cause) {
        super(msg, cause);
        this.status = status.value();
    }

}
