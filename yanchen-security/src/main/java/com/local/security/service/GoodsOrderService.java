package com.local.security.service;

import com.local.security.entity.GoodsOrder;
import org.springframework.data.domain.Page;

/**
 * @Create-By: yanchen 2020/12/26 09:10
 * @Description: TODO
 */
public interface GoodsOrderService {

    void save(Integer account,String goods);

    Page<GoodsOrder> getPage(Integer account, String goods, int offset, int limit);
}
