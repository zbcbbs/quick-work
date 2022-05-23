package com.dongzz.quick.common.exception.handler;

import com.dongzz.quick.common.exception.ApplicationException;
import com.dongzz.quick.common.exception.AuthenticationException;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.domain.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    /**
     * 系统异常
     */
    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseVo handleException(ApplicationException exception) {
        logger.error("系统异常：", exception);
        return new ResponseVo(exception.getStatus(), exception.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseVo handleException(ServiceException exception) {
        logger.error("业务异常：", exception);
        return new ResponseVo(exception.getStatus(), exception.getMessage());
    }

    /**
     * 认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseVo handleException(AuthenticationException exception) {
        logger.error("认证异常：", exception);
        return new ResponseVo(exception.getStatus(), exception.getMessage());
    }

    /**
     * 拒绝访问异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseVo handleException(AccessDeniedException exception) {
        logger.error("拒绝访问：", exception);
        return new ResponseVo(HttpStatus.FORBIDDEN.value(), "访问被拒绝，请联系管理员！");
    }


    /**
     * 服务器内部异常
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseVo handleException(Throwable throwable) {
        logger.error("服务器内部异常：", throwable);
        return new ResponseVo(HttpStatus.INTERNAL_SERVER_ERROR.value(), throwable.getMessage());

    }

}
