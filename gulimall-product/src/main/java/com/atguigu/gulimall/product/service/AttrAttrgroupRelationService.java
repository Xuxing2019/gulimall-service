package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.AttrAttrGroupRelationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;

import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 18:36:00
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void insert(AttrAttrgroupRelationEntity attrAttrgroupRelationEntity);

    AttrAttrgroupRelationEntity selectByAttrId(Long attrId);

    void deleteByAttrIdAndAttrGroupId(AttrAttrGroupRelationVo[] attrAttrGroupRelationVo);
}

