package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrAttrgroupRelationService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.AttrAttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrGroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;



/**
 * 属性分组
 *
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 18:36:00
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);
        return R.ok().put("page", page);
    }
    /**
     * 未关联属性列表
     */
    @RequestMapping("/{attrGroupId}/attr/relation")
    //@RequiresPermissions("product:attrgroup:list")
    public R attrRelationByAttrGroupId(@RequestParam Map<String, Object> params, @PathVariable("attrGroupId") Long attrGroupId){
        List<AttrEntity> attrEntities = attrGroupService.queryAttrPage(params, attrGroupId);
        return R.ok().put("data", attrEntities);
    }

    /**
     * 已关联属性列表
     * @param params
     * @param attrGroupId
     * @return
     */
    @RequestMapping("/{attrGroupId}/noattr/relation")
    //@RequiresPermissions("product:attrgroup:list")
    public R attrRelation(@RequestParam Map<String, Object> params, @PathVariable("attrGroupId") Long attrGroupId){
        PageUtils page = attrGroupService.queryNoAttrPage(params, attrGroupId);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }

    /**
     * 查询该分类属性分组，以及该分组下的属性
     */
    @RequestMapping("/{catelogId}/withattr")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@PathVariable("catelogId") Long catelogId){
        List<AttrGroupVO> data = attrGroupService.queryAttrGroups(catelogId);
        return R.ok().put("data", data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<Long> catelogPath = categoryService.selectLinkByCatId(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/attr/relation/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R relationDelete(@RequestBody AttrAttrGroupRelationVo[] attrAttrGroupRelationVo){
        attrAttrgroupRelationService.deleteByAttrIdAndAttrGroupId(attrAttrGroupRelationVo);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/attr/relation")
    //@RequiresPermissions("product:attrgroup:delete")
    public R attrRelation(@RequestBody AttrAttrGroupRelationVo[] attrAttrGroupRelationVos){
        attrGroupService.addAttrAttrGroupRelation(attrAttrGroupRelationVos);
        return R.ok();
    }

}
