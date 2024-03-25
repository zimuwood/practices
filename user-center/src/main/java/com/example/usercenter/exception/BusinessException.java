package com.example.usercenter.exception;

import com.example.usercenter.Constant.ErrorCode;
import lombok.Data;

/**
 * @author CloudyW
 * @version 1.0
 */
@Data
public class BusinessException extends RuntimeException{

    private int code;

    private String description;

    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }
}
