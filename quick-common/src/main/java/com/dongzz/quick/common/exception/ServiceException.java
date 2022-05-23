package com.dongzz.quick.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 业务异常
 */
@Getter
public class ServiceException extends RuntimeException {

    private Integer status = BAD_REQUEST.value();

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ServiceException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }

    public ServiceException(HttpStatus status, String msg, Throwable cause) {
        super(msg, cause);
        this.status = status.value();
    }

}
