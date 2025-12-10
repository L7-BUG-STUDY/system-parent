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
	public static final String ZH_CN = "zh_CN";
	public static final String EN_US = "en_US";
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
