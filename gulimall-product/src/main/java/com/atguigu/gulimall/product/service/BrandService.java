package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.dao.BrandDao;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.BrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 品牌
 *
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 18:36:00
 */
public interface BrandService extends IService<BrandEntity> {
    PageUtils queryPage(Map<String, Object> params);

    BrandEntity selectById(Long brandId);

    void updateDetailById(BrandEntity brand);

    PageUtils queryPage(Map<String, Object> params, Long catId);
}

