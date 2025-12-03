package com.l7bug.system.dto.response;

import lombok.Data;

import java.util.Map;

/**
 * Meta
 *
 * @author Administrator
 * @since 2025/12/3 18:13
 */
@Data
public class Meta {
	private Map<String, String> title;
	private String icon;
}
