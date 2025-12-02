package com.l7bug.system.mybatis.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.l7bug.database.dataobject.BaseDo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 菜单表
 *
 * @author Administrator
 * @TableName system_menu
 * @since 2025/12/2 18:09
 */
@Data
@TableName(value = "system_menu")
@EqualsAndHashCode(callSuper = true)
public class SystemMenu extends BaseDo {

	@Serial
	private static final long serialVersionUID = 7896055558228823831L;
	/**
	 * 父id
	 */
	private Long fatherId;

	/**
	 * 全路径id
	 */
	private String fullId;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 组件路径
	 */
	private String component;

	/**
	 * 菜单类型
	 */
	private String type;

	/**
	 * 元数据
	 */
	private String meta;

	/**
	 * 启用状态
	 */
	private Boolean enable;

	/**
	 * 排序值
	 */
	private Integer sort;
}
