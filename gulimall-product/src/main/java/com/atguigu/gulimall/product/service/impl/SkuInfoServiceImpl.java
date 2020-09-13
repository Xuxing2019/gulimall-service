package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.vo.spu.Skus;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.SkuInfoDao;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        key: this.dataForm.key,
//                catelogId: this.dataForm.catelogId,
//                brandId: this.dataForm.brandId,
//                min: this.dataForm.price.min,
//                max: this.dataForm.price.max
//
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isBlank(key)) {
            wrapper.and(obj->{
               obj.like("sku_name", key).or().like("sku_desc", key);
            });
        }
        int catelogId = Integer.parseInt((String) params.get("catelogId"));
        if (catelogId > 0) {
            wrapper.eq("catalog_id", catelogId);
        }
        int brandId = Integer.parseInt((String) params.get("brandId"));
        if (brandId > 0) {
            wrapper.eq("brand_id", brandId);
        }
        BigDecimal min = new BigDecimal(String.valueOf(params.get("min")));
        if (min.compareTo(new BigDecimal(0)) == 1) {
            wrapper.gt("price", min);
        }
        BigDecimal max = new BigDecimal(String.valueOf(params.get("max")));
        if (min.compareTo(new BigDecimal(0)) == 1) {
            wrapper.lt("price", max);
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void saveBaseSkuInfo(Long spuId, List<Skus> skus) {
        if (skus != null && skus.size() > 0) {

        }
    }

}