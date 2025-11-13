package com.l7bug.system.domain.role;

import lombok.Data;

/**
 * Role
 *
 * @author Administrator
 * @since 2025/11/13 11:48
 */
@Data
public class Role {
	private Long id;
	private String fatherCode;
	private String code;
	private String fullCode;
	private String name;
	private String fullName;
	private String remark;
	private RuleStatus status;
}
