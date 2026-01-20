package com.l7bug.system.dto.response;

import com.l7bug.system.dto.request.MenuNodeRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

/**
 * 节点响应信息
 *
 * @author Administrator
 * @since 2025/12/3 18:01
 */
@Getter
@Setter
public class MenuNodeResponse extends MenuNodeRequest implements TdTreeNodeResponse<MenuNodeResponse> {
	private Long id;

	private List<MenuNodeResponse> children;

	@Override
	public String getLabel() {
		return this.getName();
	}

	@Override
	public String getValue() {
		return Optional.ofNullable(this.getId()).map(Object::toString).orElse("");
	}
}
