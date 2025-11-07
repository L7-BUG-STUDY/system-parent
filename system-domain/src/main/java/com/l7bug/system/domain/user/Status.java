package com.l7bug.system.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Status
 *
 * @author Administrator
 * @since 2025/11/7 10:37
 */
@Getter
@AllArgsConstructor
public enum Status {
    ENABLE(1),
    DISABLE(0);
    private final int value;
}
