package com.l7bug.system.dto.request;

import com.l7bug.system.dto.base.MenuType;
import com.l7bug.system.dto.response.Meta;
import lombok.Getter;
import lombok.Setter;

/**
 * 节点请求对象
 *
 * @author Administrator
 * @since 2025/12/3 18:01
 */
@Getter
@Setter
public class MenuNodeRequest {
	/**
	 * 父id
	 */
	private Long fatherId;
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
	private MenuType type;

	/**
	 * 启用状态
	 */
	private Boolean enable;

	/**
	 * 排序值
	 */
	private Integer sort;

	/**
	 * 元数据
	 */
	private Meta meta;
}
