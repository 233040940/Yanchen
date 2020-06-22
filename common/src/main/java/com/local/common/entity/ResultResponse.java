package com.local.common.entity;

import com.local.common.enums.ResponseStatus;
import lombok.*;

import java.io.Serializable;

/**
 * @author yc   响应消息体
 * @project yanchen
 * @date 2020-06-15 12:54
 */

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResultResponse<T>  implements Serializable {

    private String msg;
    private T data;
    private Integer code;
    private ResponseStatus status;
}
