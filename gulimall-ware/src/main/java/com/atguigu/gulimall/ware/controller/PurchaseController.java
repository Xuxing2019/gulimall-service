package com.atguigu.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;



/**
 * �ɹ���Ϣ
 *
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 20:28:30
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 查询所有未分配采购单列表
     */
    @RequestMapping("/unreceive/list")
    //@RequiresPermissions("ware:purchase:list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceive(params);
        return R.ok().put("page", page);
    }

    /**
     * 采购人员所分配的采购单
     */
    @RequestMapping("/purchasing/staff/note/{userId}")
    //@RequiresPermissions("ware:purchase:list")
    public R purchaseOrder(@RequestBody Map<String, Object> params, @PathVariable("userId") Long userId){
        PageUtils page = purchaseService.queryPagePurchaseNote(params, userId);
        return R.ok().put("page", page);
    }

    /**
     * 采购人员领取采购单
     */
    @RequestMapping("/user/get/{purchaseId}")
    public R getPurchase(@PathVariable("purchaseId") Long purchaseId){
        purchaseService.getPurchase(purchaseId);
        return R.ok();
    }

    /**
     * 采购人员领取采购单
     */
    @RequestMapping("/accomplish")
    public R purchaseAccomplish(@PathVariable("purchaseId") Long purchaseId){
        purchaseService.accomplishPurchase(purchaseId);
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
