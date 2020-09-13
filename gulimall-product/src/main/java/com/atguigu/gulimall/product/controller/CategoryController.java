package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;



/**
 * 商品三级分类
 * @author xuxing
 * @email xuxing_2019@163.com
 * @date 2020-06-07 18:36:00
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 商品分类列表
     */
    @RequestMapping(value = "/list/tree")
    public R listTree(){
        List<CategoryEntity> categoryEntityList = categoryService.queryListTree();
        return R.ok().put("data",categoryEntityList);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    //@RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catId") String catId){
        PageUtils page = categoryService.queryPage(params, catId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        return R.ok();
    }

    /**
     * 批量修改
     */
    @RequestMapping("/update/batch")
    //@RequiresPermissions("product:category:update")
    public R updateBatch(@RequestBody(required = false) CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/batch")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
//		categoryService.removeByIds(Arrays.asList(catIds));
//      这里我们使用Mybatis-plus的逻辑删除
        categoryService.removeCategoryByIds(Arrays.asList(catIds));
        return R.ok();
    }

    /**
     * 批量删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R deleteBatch(@RequestBody Long[] catIds){
//		categoryService.removeByIds(Arrays.asList(catIds));
//      这里我们使用Mybatis-plus的逻辑删除
        categoryService.removeCategoryByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
