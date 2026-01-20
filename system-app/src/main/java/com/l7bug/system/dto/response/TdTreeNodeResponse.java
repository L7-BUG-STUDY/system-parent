package com.l7bug.system.dto.response;

import java.util.List;

/**
 * TdTreeNodeResponse
 *
 * @author Administrator
 * @since 2025/12/9 12:17
 */
public interface TdTreeNodeResponse<T extends TdTreeNodeResponse<T>> {
	List<T> getChildren();

	String getLabel();

	String getValue();
}
