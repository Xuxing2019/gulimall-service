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
public class SpuSaveVO {
    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 商品描述
     */
    private String spuDescription;

    /**
     * 商品所属类别
     */
    private Long catalogId;

    /**
     * 商品所属品牌
     */
    private Long brandId;

    /**
     * 商品重量
     */
    private BigDecimal weight;

    /**
     * 商品发布状态
     */
    private int publishStatus;

    /**
     * 商品描述-图集
     */
    private List<String> decript;

    /**
     * 商品图集
     */
    private List<String> images;

    /**
     * 商品积分，以及成长值
     */
    private Bounds bounds;

    /**
     * 商品规格参数
     */
    private List<BaseAttrs> baseAttrs;

    /**
     * 商品SKU
     */
    private List<Skus> skus;


}