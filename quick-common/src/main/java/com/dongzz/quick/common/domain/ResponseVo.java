package com.dongzz.quick.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 封装 Http 标准响应格式
 */
@Setter
@Getter
@ApiModel("统一响应格式")
public class ResponseVo implements Serializable {

    @ApiModelProperty("状态码")
    private int code;
    @ApiModelProperty("提示信息")
    private String message;
    @ApiModelProperty("响应数据")
    private Object data;

    public ResponseVo(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ResponseVo(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
