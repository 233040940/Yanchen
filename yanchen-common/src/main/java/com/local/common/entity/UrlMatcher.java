package com.local.common.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yc
 * @decription Url匹配对象
 * @project yanchen
 * @date 2020-06-12 07:11
 */

@AllArgsConstructor
@EqualsAndHashCode
public class  UrlMatcher {

    private String url;
    private RequestMethod  methodType;

}
