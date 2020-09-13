package com.atguigu.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ??????
 * 
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 20:28:30
 */
@Data
@TableName("wms_purchase")
public class PurchaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;
	/**
	 * 采购人ID
	 */
	private Long assigneeId;
	/**
	 * 采购人名称
	 */
	private String assigneeName;
	/**
	 * 采购人手机号
	 */
	private String phone;
	/**
	 * 优先级
	 */
	private Integer priority;
	/**
	 * 状态[0新建，1已分配，2正在采购，3已完成，4采购失败]
	 */
	private Integer status;
	/**
	 * 仓库ID
	 */
	private Long wareId;
	/**
	 * 金额
	 */
	private BigDecimal amount;

	private Date createTime;

	private Date updateTime;

}
