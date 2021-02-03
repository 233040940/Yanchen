package com.local.common.entity;

import com.local.common.enums.ResponseStatus;
import com.local.common.utils.JsonHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * @author yc   响应消息体
 * @project yanchen
 * @date 2020-06-15 12:54
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResultResponse<T>  implements Serializable {

    private String msg;
    private T data;
    private Integer code;
    private ResponseStatus status;
    private Long count;

    public static void outPut(HttpServletResponse response,ResultResponse result){
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer=null;
        try {
             writer = response.getWriter();
             writer.write(JsonHelper.serialize(result));
             writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
    }

}
