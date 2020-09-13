package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.INPUT;

/**
 * spu信息介绍
 * 
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 18:36:00
 */
@Data
@TableName("pms_spu_info_desc")
public class SpuInfoDescEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableId(type = INPUT)
	private Long spuId;
	/**
	 * 商品介绍
	 */
	private String decript;

}
