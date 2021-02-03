package com.local.security.service;

/**
 * @Create-By: yanchen 2021/1/10 01:46
 * @Description: TODO
 */
public interface AccountDetailService {

    void save(Integer accountID,boolean banned);


    /**
     * @create-by: yanchen 2021/1/10 01:48
     * @description: 查询账号是否被禁用
     * @param accountID  账号ID
     * @return: boolean
     */      
    boolean banned(Integer accountID);
}
