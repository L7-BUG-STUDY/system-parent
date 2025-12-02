package com.l7bug.system.domain.menu;

import lombok.Data;

import java.util.Map;

/**
 * MetaVal
 *
 * @author Administrator
 * @since 2025/12/2 16:15
 */
@Data
public class MetaVal {
	/**
	 * 标题元数据,如:
	 * zh_CN=中文名
	 * en_US=英文名
	 */
	private Map<String, String> title;
	/**
	 * 图标
	 */
	private String icon;
}
