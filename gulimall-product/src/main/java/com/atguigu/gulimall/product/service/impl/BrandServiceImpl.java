package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

import com.atguigu.gulimall.product.dao.BrandDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("brand_id", key).or((obj)->{
                obj.like("name", key);
            });
        }
        IPage<BrandEntity> page = this.page(new Query<BrandEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public BrandEntity selectById(Long brandId) {
        BrandEntity brandEntity = brandDao.selectById(brandId);
        return brandEntity;
    }

    @Override
    public void updateDetailById(BrandEntity brand) {
        boolean b = this.updateById(brand);
        if (!StringUtils.isEmpty(brand.getName())){
            UpdateWrapper<CategoryBrandRelationEntity> wrapper = new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brand.getBrandId()).set("brand_name", brand.getName());
            categoryBrandRelationService.update(wrapper);
        }
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        // 1.查询品牌和分类的中间表，获得该分类相关联的品牌ID集合
        QueryWrapper<CategoryBrandRelationEntity> catelog_id = new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId);
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = categoryBrandRelationService.getBaseMapper().selectList(catelog_id);

        List<Long> brandIds = categoryBrandRelationEntities.stream().map(obj -> {
            return obj.getBrandId();
        }).collect(Collectors.toList());

        QueryWrapper<BrandEntity> brand_id = new QueryWrapper<BrandEntity>();
        if (brandIds != null && brandIds.size() > 0) {
            brand_id.in("brand_id", brandIds);
        } else {
            brand_id.eq("brand_id", -1L);
        }
        IPage<BrandEntity> brandEntityIPage = this.getBaseMapper().selectPage(this.page(new Query<BrandEntity>().getPage(params)), brand_id);
        return new PageUtils(brandEntityIPage);
    }
}