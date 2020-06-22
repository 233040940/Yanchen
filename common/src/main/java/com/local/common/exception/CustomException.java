package com.local.common.exception;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 自定义exception
 * @date 2020-06-17 22:58
 */
public class CustomException extends RuntimeException {

    private String message;

    public CustomException(String message){

        super(message);
        this.message=message;
    }

    public String getMessage(){
        return message;
    }
}
