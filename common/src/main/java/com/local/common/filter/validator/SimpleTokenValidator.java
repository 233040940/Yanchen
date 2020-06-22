package com.local.common.filter.validator;

import com.local.common.utils.JwtProvider;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 只对token字符串是否为本服务器生成的token进行校验
 * @date 2020-06-15 12:40
 */
public class SimpleTokenValidator extends AbstractTokenValidator {

    @Override
   public boolean tokenEffective(String token) {

        return JwtProvider.checkToken(token);
    }
}
