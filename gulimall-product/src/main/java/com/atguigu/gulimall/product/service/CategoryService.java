package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 18:36:00
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> queryListTree();

    void removeCategoryByIds(List<Long> asList);

    List<Long> selectLinkByCatId(Long catelogId);

    CategoryEntity selectById(Long catelogId);

    PageUtils queryPage(Map<String, Object> params, String catId);
}

