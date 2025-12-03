package com.l7bug.system.dto.response;

import lombok.Data;

import java.util.List;

/**
 * MenuNode
 *
 * @author Administrator
 * @since 2025/12/3 18:01
 */
@Data
public class MenuNodeResponse {
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
	 * 启用状态
	 */
	private Boolean enable;

	/**
	 * 排序值
	 */
	private Integer sort;

	private Meta meta;

	private List<MenuNodeResponse> children;
}
