package com.l7bug.system.domain.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * Role
 *
 * @author Administrator
 * @since 2026/1/16 18:16
 */
@Data
public class Role implements Comparable<Role> {
	public static final String ROOT_CODE = "root";
	public static final String PATH_SEPARATOR = "/";
	@Getter(AccessLevel.PRIVATE)
	private final RoleGateway roleGateway;
	private Long id;
	/**
	 * 角色编码
	 */
	@NotBlank
	private String code;
	/**
	 * 父级角色编码
	 */
	private String fatherCode;
	/**
	 * 角色状态
	 */
	@NotNull
	private RoleStatus status = RoleStatus.ENABLED;
	/**
	 * 角色名称
	 */
	@NotBlank
	private String name;
	/**
	 * 全路径编码
	 */
	private String fullCode;

	private Integer sort = 0;

	/**
	 * 将角色状态设置为禁用
	 * 此方法会将当前角色的status字段更新为RoleStatus.DISABLED
	 */
	public void disabled() {
		this.status = RoleStatus.DISABLED;
	}

	/**
	 * 将角色状态设置为启用
	 * 此方法会将当前角色的status字段更新为RoleStatus.ENABLED
	 */
	public void enabled() {
		this.status = RoleStatus.ENABLED;
	}

	public void save() {
		this.roleGateway.save(this);
	}

	@Override
	public int compareTo(Role o) {
		int compareTo = this.sort.compareTo(o.getSort());
		if (compareTo == 0) {
			return this.getId().compareTo(o.getId());
		}
		return compareTo;
	}
}
