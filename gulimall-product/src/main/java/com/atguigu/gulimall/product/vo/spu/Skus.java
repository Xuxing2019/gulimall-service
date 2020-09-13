/**
  * Copyright 2020 bejson.com 
  */
package com.atguigu.gulimall.product.vo.spu;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2020-09-12 20:54:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Data
public class Skus {

    /**
     * 属性集合
     */
    private List<Attr> attr;
    /**
     * sku名字
     */
    private String skuName;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 标题
     */
    private String skuTitle;
    /**
     * 子标题
     */
    private String skuSubtitle;
    /**
     * 图片
     */
    private List<Images> images;
    private List<String> descar;
    /**
     * 满件折扣
     */
    private int fullCount;
    /**
     * 满件折扣值
     */
    private int discount;
    /**
     * 是否可叠加优惠
     */
    private int countStatus;
    /**
     * 满减价格
     */
    private BigDecimal fullPrice;
    /**
     * 满减价优惠值
     */
    private BigDecimal reducePrice;
    /**
     * 是否可叠加优惠
     */
    private int priceStatus;
    /**
     * 会员相关价格
     */
    private List<MemberPrice> memberPrice;
}