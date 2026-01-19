package com.l7bug.system.domain.role;

import lombok.Data;

/**
 * Role
 *
 * @author Administrator
 * @since 2026/1/16 18:16
 */
@Data
public class Role {
	/**
	 * 角色编码
	 */
	private String code;
	/**
	 * 角色状态
	 */
	private RoleStatus status;
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 全路径编码
	 */
	private String fullCode;
}
