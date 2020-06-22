package com.local.common.enums;

/**
 * @author ResponseCode
 * @version 1.0
 * @date 2020-06-15 13:04
 */
public enum ResponseCode {

    POST(ResponseStatus.SUCCESS,201),
    GET(ResponseStatus.SUCCESS,200),
    DELETE(ResponseStatus.SUCCESS,410),
    PUT(ResponseStatus.SUCCESS,201),
    UNAUTHORIZED(ResponseStatus.ERROR,401),
    CLIENT_FAIL(ResponseStatus.ERROR,400),
    SERVER_FAIL(ResponseStatus.ERROR,500);

    private Integer code;

    private ResponseStatus status;

    ResponseCode(ResponseStatus status,Integer code){
        this.code=code;
        this.status=status;
    }

    public Integer getCode(){

        return code;
    }
}
