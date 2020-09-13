package com.atguigu.gulimall.ware.vo;

import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;
import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 * @date 2020/9/13
 */
@Data
public class PurchaseDetailVo {
    private Long purchaseId;
    private List<Long> purchaseDetails;
}
