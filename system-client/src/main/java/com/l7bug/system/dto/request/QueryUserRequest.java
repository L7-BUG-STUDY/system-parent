package com.l7bug.system.dto.request;

import com.l7bug.common.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * QueryUserRequest
 *
 * @author Administrator
 * @since 2025/11/14 16:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryUserRequest extends PageQuery {
	private String username;
}
