package com.atguigu.gulimall.product.entity;

import com.atguigu.common.valid.AddGroup;
import com.atguigu.common.valid.UpdateGroup;
import com.atguigu.common.valid.UpdateStatus;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author xuxing
 * @email Xuxing_2019@163.com
 * @date 2020-06-07 18:36:00
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(message = "品牌ID不能为空", groups = {UpdateGroup.class,UpdateStatus.class})
	@Null(message = "添加数据不能携带品牌ID", groups = {AddGroup.class})
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotNull(message = "请填写品牌名称", groups = {AddGroup.class})
	@NotBlank(message = "请填写品牌名称", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "logo地址不能为空", groups = {AddGroup.class})
	@URL(message = "logo必须为一个url链接地址", groups = {UpdateGroup.class, AddGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "状态不能为空", groups = {AddGroup.class, UpdateStatus.class})
	@PositiveOrZero(message = "必须为1或0", groups = {UpdateGroup.class, AddGroup.class,UpdateStatus.class})
	@Max(value = 1, groups = {UpdateGroup.class, AddGroup.class,UpdateStatus.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@Pattern(regexp = "^[a-zA-Z]$",message = "检索首字母必须为a-zA-Z", groups = {AddGroup.class, UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序字段不能为空", groups = {AddGroup.class})
	@PositiveOrZero(message = "必须为正数或0", groups = {UpdateGroup.class, AddGroup.class})
	@Min(value = 0, groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;

}
