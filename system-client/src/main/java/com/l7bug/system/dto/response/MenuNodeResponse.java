package com.l7bug.system.dto.response;

import com.l7bug.system.dto.request.MenuNodeRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 节点响应信息
 *
 * @author Administrator
 * @since 2025/12/3 18:01
 */
@Getter
@Setter
public class MenuNodeResponse extends MenuNodeRequest {
	private Long id;

	private List<MenuNodeRequest> children;
}
