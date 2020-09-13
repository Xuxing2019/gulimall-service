package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrAttrgroupRelationService;
import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.gulimall.product.vo.AttrAttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrGroupVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    AttrService attrService;
    @Autowired
    AttrDao attrDao;
    @Autowired
    AttrGroupDao attrGroupDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if (catelogId == 0){
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
            String key = (String) params.get("key");
            if (StringUtils.isEmpty(key)) {
                wrapper.like("attr_group_name", key).like("descript", key);
            }
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        } else {
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId);
            String key = (String) params.get("key");
            if (!StringUtils.isEmpty(key)){
                wrapper.and((obj) -> {
                    obj.like("attr_group_name", key).like("descript", key);
                });
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }
    }

    @Override
    public PageUtils queryNoAttrPage(Map<String, Object> params, Long attrGroupId) {
        // 1.查询出当前分组所在的分类
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        // 2.查询当前分类下的所有分组
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        // 这里的ids应该也要进行空值判断
        List<Long> attrGroupIds = attrGroupEntities.stream().map(obj -> {
            return obj.getAttrGroupId();
        }).collect(Collectors.toList());

        // 3.查询出这些分组所关联的属性attrIds
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map(obj -> {
            return obj.getAttrId();
        }).collect(Collectors.toList());

        // 4.查询出当前分类下所有未分配分组的基本属性(不包括销售属性)
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type", 1);
        if (attrIds != null && attrIds.size() > 0) {
            attrEntityQueryWrapper.notIn("attr_id", attrIds);
        }
        // 外加关键字查询
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            attrEntityQueryWrapper.and(obj->{
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), attrEntityQueryWrapper);
        return new PageUtils(page);
    }

    @Override
    public void addAttrAttrGroupRelation(AttrAttrGroupRelationVo[] params) {
        List<AttrAttrgroupRelationEntity> collect = Arrays.asList(params).stream().map(obj -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(obj, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        attrAttrgroupRelationService.saveBatch(collect);
    }

    @Override
    public List<AttrEntity> queryAttrPage(Map<String, Object> params, Long attrGroupId) {
        // 查询当前分组已关联的属性
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId);
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(wrapper);
        List<Long> collect = attrAttrgroupRelationEntities.stream().map((obj) -> {
            return obj.getAttrId();
        }).collect(Collectors.toList());
        // 空集合传参会导致sql异常
        List<AttrEntity> attrEntities = null;
        if (collect != null && collect.size() > 0) {
            attrEntities = attrDao.selectBatchIds(collect);
        }
        return attrEntities;
    }

    @Override
    public List<AttrGroupVO> queryAttrGroups(Long catelogId) {

        // 1.查询该分类下的所有分组
        QueryWrapper<AttrGroupEntity> catelog_id = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId);
        List<AttrGroupEntity> attrGroupEntities = this.getBaseMapper().selectList(catelog_id);

        // 2.查询各个分组下的属性
        List<AttrGroupVO> attrGroupVOS = attrGroupEntities.stream().map(obj -> {
            // 2.1创建视图对象
            AttrGroupVO attrGroupVO = new AttrGroupVO();
            BeanUtils.copyProperties(obj, attrGroupVO);
            // 2.2根据中间表查询出该分组下的所有属性id集合
            QueryWrapper<AttrAttrgroupRelationEntity> attr_group_id = new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", obj.getAttrGroupId());
            List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(attr_group_id);
            List<Long> attrIds = attrAttrgroupRelationEntities.stream().map(attrAttrgroupRelation -> {
                // 销售属性不展示
                return attrAttrgroupRelation.getAttrId() == 0 ? -1 : attrAttrgroupRelation.getAttrId();
            }).collect(Collectors.toList());

            // 2.3根据ids查出所有属性并且封装到视图对象
            if (attrIds != null && attrIds.size() > 0) {
                List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);
                attrGroupVO.setAttrs(attrEntities);
            } else {
                attrGroupVO.setAttrs(new ArrayList<AttrEntity>());
            }

            return attrGroupVO;
        }).collect(Collectors.toList());
        return attrGroupVOS;
    }
}