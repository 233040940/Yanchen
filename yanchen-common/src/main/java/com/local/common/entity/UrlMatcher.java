package com.local.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yc
 * @decription Url匹配对象
 * @project yanchen
 * @date 2020-06-12 07:11
 */

@AllArgsConstructor
@Getter
public class  UrlMatcher {

    private String url;
    private RequestMethod methodType;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof UrlMatcher) {
            UrlMatcher urlMatcher = (UrlMatcher) obj;
           return match(urlMatcher.getUrl(),urlMatcher.getMethodType());
        }
        return false;
    }

    private boolean match(String url, RequestMethod methodType) {
        //模糊匹配
        if(this.url.matches(url) && this.methodType.equals(methodType)){
            return true;
        }
        return false;
    }
}