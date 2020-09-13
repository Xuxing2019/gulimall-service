package com.atguigu.gulimall.product.feign.coupon;

import com.atguigu.common.to.SkuReductionLadderMemberTo;
import com.atguigu.common.to.SpuBoundsTo;
import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xuxing
 * @date 2020/9/13
 */
@FeignClient("gulimall-coupon")
public interface SpuCouponApi {

    /**
     * 调用远程服务的流程
     * 1.调用方封装好请求体，调用远程接口，使用@RequestBody注解将请求参数对象转换成Json，放置到请求体中
     * 2.被调用方接受Json参数，使用@RequestBody注解 解析Json封装成参数对象，理论上讲，只要Json转换不出错就可以进行参数传递
     * ps：按照规范编写 需要使用TO（跨服务调用数据传输对象）进行参数传递
     * @param spuBoundsTo
     * @return
     */

    @RequestMapping("/coupon/spubounds/save/bounds")
    public R saveBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    /**
     * 保存sku折扣满减信息
     */
    @RequestMapping("/coupon/skuladder/save/reduction/ladder")
    //@RequiresPermissions("coupon:skuladder:save")
    public R saveSkuReductionLadder(@RequestBody SkuReductionLadderMemberTo skuReductionLadderMemberTo);
}
