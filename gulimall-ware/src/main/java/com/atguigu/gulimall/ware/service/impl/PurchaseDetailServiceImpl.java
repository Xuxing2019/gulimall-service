package com.atguigu.gulimall.ware.service.impl;

import com.atguigu.gulimall.ware.constant.WareConstant;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import com.atguigu.gulimall.ware.vo.PurchaseDetailVo;
import org.apache.commons.lang.StringUtils;
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

import com.atguigu.gulimall.ware.dao.PurchaseDetailDao;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;
import com.atguigu.gulimall.ware.service.PurchaseDetailService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Autowired
    PurchaseService purchaseService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> purchaseDetailEntityQueryWrapper = new QueryWrapper<>();

        String wareId = (String) params.get("wareId");
        if (!StringUtils.isBlank(wareId)) {
            purchaseDetailEntityQueryWrapper.eq("ware_id", wareId);
        }

        String status = (String) params.get("status");
        if (!StringUtils.isBlank(status)) {
            purchaseDetailEntityQueryWrapper.eq("status", status);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isBlank(key)) {
            purchaseDetailEntityQueryWrapper.and(obj->{
               obj.like("sku_id", key).or().like("purchase_id", key);
            });
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                purchaseDetailEntityQueryWrapper
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchaseDetail(PurchaseDetailVo purchaseDetailVo) {
        // 1.创建采购单
        Long purchaseId = purchaseDetailVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            Date date = new Date();
            purchaseEntity.setCreateTime(date);
            purchaseEntity.setUpdateTime(date);
            purchaseEntity.setPriority(0);
            purchaseEntity.setStatus(WareConstant.PurchaseEnum.NEW_BUILT.getCode());
            purchaseService.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        // 2.无论是否创建新的采购单 都需要对采购需求进行更新
        List<Long> purchaseDetails = purchaseDetailVo.getPurchaseDetails();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = purchaseDetails.stream().map(obj -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(obj);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailEnum.ASSIGNED.getCode());
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        this.updateBatchById(collect);
    }

}