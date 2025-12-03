package com.l7bug.system.dto.response;

import lombok.Data;

/**
 * MenuNode
 *
 * @author Administrator
 * @since 2025/12/3 18:01
 */
@Data
public class MenuNodeResponse {
	private String path;
	private String name;
	private String component;
	private Meta meta;
}
