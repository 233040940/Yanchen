package com.local.security.common;

import com.local.common.entity.ResultResponse;
import com.local.common.enums.ResponseStatus;
import com.local.common.exception.CustomException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Create-By: yanchen 2021/1/10 05:39
 * @Description: TODO
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResultResponse handleCustomException(CustomException e){
        return ResultResponse.builder().code(400).msg(e.getMessage()).status(ResponseStatus.ERROR).build();
    }
}
