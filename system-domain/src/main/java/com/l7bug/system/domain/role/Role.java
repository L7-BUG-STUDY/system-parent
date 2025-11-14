package com.l7bug.system.domain.role;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Role
 *
 * @author Administrator
 * @since 2025/11/13 11:48
 */
@Data
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Role {
	private final RoleGateway roleGateway;
	private Long id;
	/**
	 * 上级编码
	 */
	private String fatherCode;

	/**
	 * 角色标识
	 */
	private String code;

	/**
	 * 全路径名称
	 */
	private String fullCode;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 全路径名称
	 */
	private String fullName;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 状态
	 */
	private RoleStatus status;
}
