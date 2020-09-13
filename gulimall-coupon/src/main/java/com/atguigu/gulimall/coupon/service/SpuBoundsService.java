package com.atguigu.gulimall.coupon.service;

import com.atguigu.common.to.SpuBoundsTo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 20:16:39
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBounds(SpuBoundsTo spuBoundsTo);
}

