package com.l7bug.system.domain.role;

import lombok.Getter;

/**
 * RuleStatus
 *
 * @author Administrator
 * @since 2025/11/13 12:00
 */
public enum RuleStatus {
	DISABLED,
	ENABLED,
	;
	@Getter
	private final int value;

	RuleStatus() {
		this.value = ordinal();
	}
}
