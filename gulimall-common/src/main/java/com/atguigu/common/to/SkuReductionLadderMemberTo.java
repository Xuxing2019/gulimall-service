package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xuxing
 * @date 2020/9/13
 */
@Data
public class SkuReductionLadderMemberTo {

    private Long skuId;
    /**
     * 满件折扣
     */
    private int fullCount;
    /**
     * 满件折扣值
     */
    private BigDecimal discount;
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
