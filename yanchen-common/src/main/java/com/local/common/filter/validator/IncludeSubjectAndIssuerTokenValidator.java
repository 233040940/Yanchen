package com.local.common.filter.validator;

import com.local.common.utils.JwtProvider;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 包含token，接收对象和签发对象验证器
 * @date 2020-06-15 17:48
 */
public class IncludeSubjectAndIssuerTokenValidator extends AbstractTokenValidator {

    private String subject;   //接收对象

    private String issuer;    //签发对象

    public IncludeSubjectAndIssuerTokenValidator(String subject, String issuer){
        this.subject=subject;
        this.issuer=issuer;
    }
    @Override
    public boolean tokenEffective(String token) {
        return JwtProvider.checkToken(token,subject,issuer);
    }
}
