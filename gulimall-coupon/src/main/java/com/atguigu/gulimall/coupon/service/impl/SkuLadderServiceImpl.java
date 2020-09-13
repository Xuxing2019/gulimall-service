package com.atguigu.gulimall.coupon.service.impl;

import com.atguigu.common.constant.SkuConstant;
import com.atguigu.common.to.MemberPrice;
import com.atguigu.common.to.SkuReductionLadderMemberTo;
import com.atguigu.gulimall.coupon.entity.MemberPriceEntity;
import com.atguigu.gulimall.coupon.entity.SkuFullReductionEntity;
import com.atguigu.gulimall.coupon.service.MemberPriceService;
import com.atguigu.gulimall.coupon.service.SkuFullReductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.coupon.dao.SkuLadderDao;
import com.atguigu.gulimall.coupon.entity.SkuLadderEntity;
import com.atguigu.gulimall.coupon.service.SkuLadderService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuLadderService")
public class SkuLadderServiceImpl extends ServiceImpl<SkuLadderDao, SkuLadderEntity> implements SkuLadderService {

    @Autowired
    SkuFullReductionService skuFullReductionService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuLadderEntity> page = this.page(
                new Query<SkuLadderEntity>().getPage(params),
                new QueryWrapper<SkuLadderEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSkuReductionLadder(SkuReductionLadderMemberTo skuReductionLadderMemberTo) {
        // 1.插入折扣价信息
        if (skuReductionLadderMemberTo.getFullCount() > 0) {
            SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
            skuLadderEntity.setDiscount(skuReductionLadderMemberTo.getDiscount());
            skuLadderEntity.setFullCount(skuReductionLadderMemberTo.getFullCount());
            skuLadderEntity.setPrice(skuReductionLadderMemberTo.getFullPrice());
            skuLadderEntity.setAddOther(skuReductionLadderMemberTo.getCountStatus());
            skuLadderEntity.setSkuId(skuReductionLadderMemberTo.getSkuId());
            this.save(skuLadderEntity);
        }

        // 2.插入满减价信息
        if (skuReductionLadderMemberTo.getFullPrice().compareTo(new BigDecimal(0)) == 1) {
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            skuFullReductionEntity.setFullPrice(skuReductionLadderMemberTo.getFullPrice());
            skuFullReductionEntity.setReducePrice(skuReductionLadderMemberTo.getReducePrice());
            skuFullReductionEntity.setAddOther(skuReductionLadderMemberTo.getPriceStatus());
            skuFullReductionEntity.setSkuId(skuReductionLadderMemberTo.getSkuId());
            skuFullReductionService.save(skuFullReductionEntity);
        }

        // 3.插入会员特价信息
        List<MemberPrice> memberPrice = skuReductionLadderMemberTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntities = memberPrice.stream().map(obj -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setMemberLevelId(obj.getId());
            memberPriceEntity.setMemberLevelName(obj.getName());
            memberPriceEntity.setAddOther(SkuConstant.JOIN_OTHER_OFFERS);
            memberPriceEntity.setMemberPrice(obj.getPrice());
            memberPriceEntity.setSkuId(skuReductionLadderMemberTo.getSkuId());
            return memberPriceEntity;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntities);
    }

}