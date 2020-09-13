package com.atguigu.gulimall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> queryListTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        //查 出所有根节点
        List<CategoryEntity> collect = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == 0;
        }).map((menu)->{
            menu.setChildren(treeCategoryEntity(menu,categoryEntities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        //查出所有的节点组成父子关系
        return collect;
    }

    @Override
    public void removeCategoryByIds(List<Long> asList) {
//        TODO 做删除校验

        this.removeByIds(asList);
    }

    @Override
    public List<Long> selectLinkByCatId(Long catelogId) {
        ArrayList<Long> longs = new ArrayList<>();
        selectLinkByCatIdRecursion(catelogId, longs);
        Collections.reverse(longs);
        return longs;
    }

    @Override
    public CategoryEntity selectById(Long catelogId) {
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        return categoryEntity;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, String catId) {
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", catId);
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    private List<Long> selectLinkByCatIdRecursion(Long catelogId, List<Long> longs){
        // 收集当前节点
        longs.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            selectLinkByCatIdRecursion(byId.getParentCid(), longs);
        }
        return longs;
    }

    //递归查找
    private List<CategoryEntity> treeCategoryEntity(CategoryEntity collect, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> tree = categoryEntities.stream().filter(categoryEntity -> {
            return collect.getCatId().equals(categoryEntity.getParentCid());
        }).map(categoryEntity -> {
            categoryEntity.setChildren(treeCategoryEntity(categoryEntity, categoryEntities));
            return categoryEntity;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return tree;
    }

}