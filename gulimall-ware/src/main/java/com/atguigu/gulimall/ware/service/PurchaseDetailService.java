package com.atguigu.gulimall.ware.service;

import com.atguigu.gulimall.ware.vo.PurchaseDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 20:28:30
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void mergePurchaseDetail(PurchaseDetailVo purchaseDetailVo);
}

