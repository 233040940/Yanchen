package com.local.common.filter.validator;

import com.local.common.utils.JwtProvider;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 包含token,接收对象信息的验证器
 * @date 2020-06-15 17:29
 */
public class IncludeSubjectTokenValidator extends AbstractTokenValidator {

    private String subject;     //接收对象信息

    public IncludeSubjectTokenValidator(String subject){

        this.subject=subject;
    }

    @Override
    public boolean tokenEffective(String token) {
        return JwtProvider.checkToken(token,subject);
    }
}
