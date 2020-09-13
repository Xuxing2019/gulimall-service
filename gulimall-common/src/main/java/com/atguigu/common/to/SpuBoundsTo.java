package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xuxing
 * @date 2020/9/13
 */
@Data
public class SpuBoundsTo {
    /**
     *
     */
    private Long spuId;
    /**
     * 成长积分
     */
    private BigDecimal growBounds;
    /**
     * 购物积分
     */
    private BigDecimal buyBounds;
}
