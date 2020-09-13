package com.atguigu.gulimall.ware.service.impl;

import com.atguigu.gulimall.ware.constant.WareConstant;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;
import com.atguigu.gulimall.ware.service.PurchaseDetailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.ware.dao.PurchaseDao;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> purchaseEntityQueryWrapper = new QueryWrapper<>();

        String status = (String) params.get("status");
        if (!StringUtils.isBlank(status)) {
            purchaseEntityQueryWrapper.eq("status", status);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isBlank(key)) {
            purchaseEntityQueryWrapper.and(obj->{
                obj.like("assignee_name", key).or().like("assignee_id", key);
            });
        }
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                purchaseEntityQueryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> purchaseEntityQueryWrapper = new QueryWrapper<>();

        purchaseEntityQueryWrapper.and(obj->{
            obj.eq("status", WareConstant.PurchaseEnum.NEW_BUILT.getCode()).or()
                    .eq("status", WareConstant.PurchaseEnum.ASSIGNED.getCode());
        });
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                purchaseEntityQueryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPagePurchaseNote(Map<String, Object> params, Long userId) {
        QueryWrapper<PurchaseEntity> purchaseEntityQueryWrapper = new QueryWrapper<>();
        if (userId == null) {
            purchaseEntityQueryWrapper.eq("assignee_id", "-1");
        } else {
            purchaseEntityQueryWrapper.eq("assignee_id", userId);
        }
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                purchaseEntityQueryWrapper
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void getPurchase(Long purchaseId) {
        // 1.更新采购单状态
        PurchaseEntity purchaseEntity = this.baseMapper.selectById(purchaseId);
        PurchaseEntity purchaseEntity1 = new PurchaseEntity();
        purchaseEntity1.setId(purchaseEntity.getId());
        purchaseEntity1.setStatus(WareConstant.PurchaseEnum.RECEIVED.getCode());
        this.updateById(purchaseEntity1);
        // 2.更新采购项状态
        QueryWrapper<PurchaseDetailEntity> purchaseDetailEntityQueryWrapper = new QueryWrapper<>();
        purchaseDetailEntityQueryWrapper.eq("purchase_id", purchaseId);
        List<PurchaseDetailEntity> purchaseDetailEntities =
                purchaseDetailService.getBaseMapper().selectList(purchaseDetailEntityQueryWrapper);
        List<PurchaseDetailEntity> collect = purchaseDetailEntities.stream().map(obj -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(obj.getId());
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailEnum.BE_PURCHASING.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
    }

    @Override
    public void accomplishPurchase(Long purchaseId) {
        // 1.更新采购单状态
        PurchaseEntity purchaseEntity = this.baseMapper.selectById(purchaseId);
        PurchaseEntity purchaseEntity1 = new PurchaseEntity();
        purchaseEntity1.setId(purchaseEntity.getId());
        purchaseEntity1.setStatus(WareConstant.PurchaseEnum.COMPLETED.getCode());
        this.baseMapper.updateById(purchaseEntity1);
        // 2.更新采购项状态
        QueryWrapper<PurchaseDetailEntity> purchaseDetailEntityQueryWrapper = new QueryWrapper<>();
        purchaseDetailEntityQueryWrapper.eq("purchase_id", purchaseId);
        List<PurchaseDetailEntity> purchaseDetailEntities =
                purchaseDetailService.getBaseMapper().selectList(purchaseDetailEntityQueryWrapper);
        List<PurchaseDetailEntity> collect = purchaseDetailEntities.stream().map(obj -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(obj.getId());
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailEnum.COMPLETED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);
    }

}