package com.l7bug.system.dto.request;

import com.l7bug.system.dto.base.MenuType;
import com.l7bug.system.dto.response.Meta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	@NotBlank(message = "路径不能为空")
	private String path;

	/**
	 * 名称
	 */
	@NotBlank(message = "名称不能为空")
	private String name;

	/**
	 * 组件路径
	 */
	private String component;

	/**
	 * 菜单类型
	 */
	@NotNull(message = "菜单类型不能为空")
	private MenuType type;

	/**
	 * 启用状态
	 */
	@NotNull(message = "启用状态不能为空")
	private Boolean enable;

	/**
	 * 排序值
	 */
	private Integer sort = 0;

	/**
	 * 元数据
	 */
	@NotNull(message = "元数据不能为空")
	private Meta meta;
}
