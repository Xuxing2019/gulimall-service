package com.atguigu.gulimall.coupon.service;

import com.atguigu.common.to.SkuReductionLadderMemberTo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.coupon.entity.SkuLadderEntity;

import java.util.Map;

/**
 * 商品阶梯价格
 *
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 20:16:39
 */
public interface SkuLadderService extends IService<SkuLadderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReductionLadder(SkuReductionLadderMemberTo skuReductionLadderMemberTo);
}

