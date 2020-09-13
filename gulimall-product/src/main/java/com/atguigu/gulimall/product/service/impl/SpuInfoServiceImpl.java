package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.to.SkuReductionLadderMemberTo;
import com.atguigu.common.to.SpuBoundsTo;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.product.entity.*;
import com.atguigu.gulimall.product.feign.coupon.SpuCouponApi;
import com.atguigu.gulimall.product.service.*;
import com.atguigu.gulimall.product.vo.spu.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SpuImagesService spuImagesService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    SpuCouponApi spuCouponApi;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isBlank(key)) {
            wrapper.and(obj->{
                obj.like("spu_description", key).or().like("spu_name", key);
            });
        }

        String status = (String) params.get("status");
        if (!StringUtils.isBlank(status)) {
            wrapper.eq("publish_status", status);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isBlank(brandId)) {
            wrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isBlank(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVO spuSaveVO) {
        // 1.保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVO, spuInfoEntity);
            // 设置一下创建时间和更新时间
        Date date = new Date();
        spuInfoEntity.setCreateTime(date);
        spuInfoEntity.setUpdateTime(date);
        this.saveBaseSpuInfo(spuInfoEntity);

        // 2.保存spu描述图片 pms_spu_info_desc
        List<String> decript = spuSaveVO.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(";", decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        // 3.保存spu图集  pms_spu_images
        List<String> images = spuSaveVO.getImages();
        SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
        spuImagesEntity.setSpuId(spuInfoEntity.getId());
        spuImagesEntity.setImgUrl(String.join(";", images));
        spuImagesService.save(spuImagesEntity);

        // 4.保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVO.getBaseAttrs();
        productAttrValueService.saveBaseAttrs(spuInfoEntity.getId(), baseAttrs);

        // 5.保存spu的积分信息,成长值信息  --- 该数据保存需要调用另一个服务 sms_spu_bounds
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        Bounds bounds = spuSaveVO.getBounds();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuInfoEntity.getId());
        R r = spuCouponApi.saveBounds(spuBoundsTo);
        if (r.getCode() != 0) {
            log.error("远程服务调用失败[gulimall-coupon]");
        }

        // 6.保存spu所对应的sku信息
        List<Skus> skus = spuSaveVO.getSkus();
        // 6.1 保存sku的基本信息 pms_sku_info
        skus.stream().forEach(obj -> {
            List<Images> imgs = obj.getImages();
            String defaultImage = "";
            for (Images item : imgs) {
                if (item.getDefaultImg() == 1) {
                    defaultImage = item.getImgUrl();
                }
            }
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            /**
             * skuName，skuTitle，subSkuTitle，price
             */
            BeanUtils.copyProperties(obj, skuInfoEntity);
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setBrandId(spuSaveVO.getBrandId());
            skuInfoEntity.setSkuDefaultImg(defaultImage);
            skuInfoEntity.setSkuDesc(String.join("-", obj.getDescar()));
            skuInfoService.save(skuInfoEntity);
            // 6.2 保存sku的图片 pms_sku_images
            List<SkuImagesEntity> skuImagesEntities = imgs.stream().map(img -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                if (img.getDefaultImg() == 1) {
                    skuImagesEntity.setDefaultImg(1);
                } else {
                    skuImagesEntity.setDefaultImg(0);
                }
                skuImagesEntity.setImgUrl(img.getImgUrl());
                skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                return skuImagesEntity;
            }).filter(item->{
                return !StringUtils.isBlank(item.getImgUrl());
            }).collect(Collectors.toList());
            skuImagesService.saveBatch(skuImagesEntities);

            // 6.3 保存sku的销售属性信息 pms_sku_sale_attr_value
            List<Attr> attrs = obj.getAttr();
            List<SkuSaleAttrValueEntity> saleAttrs = attrs.stream().map(attr -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(saleAttrs);

            // 6.4 保存sku的满减信息，优惠信息 --- 该数据保存需要调用另一个服务 sms_sku_full_reduction/sms_sku_ladder
            SkuReductionLadderMemberTo skuReductionLadderMemberTo = new SkuReductionLadderMemberTo();
            BeanUtils.copyProperties(obj, skuReductionLadderMemberTo);
            skuReductionLadderMemberTo.setSkuId(skuInfoEntity.getSkuId());
            R r1 = spuCouponApi.saveSkuReductionLadder(skuReductionLadderMemberTo);
            if (r1.getCode() != 0) {
                log.error("远程服务调用失败[gulimall-coupon]");
            }
        });
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.getBaseMapper().insert(spuInfoEntity);
    }

}