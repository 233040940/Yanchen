package com.local.common.filter.validator;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description  token校验器
 * @date 2020-06-15 12:38
 */
public abstract class AbstractTokenValidator {

   public abstract boolean tokenEffective(String token);    //验证token是否有效
}
