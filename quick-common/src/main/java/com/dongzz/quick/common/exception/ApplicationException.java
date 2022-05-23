package com.dongzz.quick.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 系统异常
 */
@Getter
public class ApplicationException extends RuntimeException {

    private Integer status = INTERNAL_SERVER_ERROR.value();


    public ApplicationException(String msg) {
        super(msg);
    }

    public ApplicationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ApplicationException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }

    public ApplicationException(HttpStatus status, String msg, Throwable cause) {
        super(msg, cause);
        this.status = status.value();
    }

}
