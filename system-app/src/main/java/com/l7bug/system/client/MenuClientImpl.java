package com.l7bug.system.client;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuGateway;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MenuClientImpl
 *
 * @author Administrator
 * @since 2025/12/4 18:18
 */
@Component
@AllArgsConstructor
public class MenuClientImpl implements MenuClient {
	private final MenuGateway menuGateway;

	@Override
	public List<MenuNodeRequest> getAllRootNodes() {
		List<Menu> allRootNode = menuGateway.findAllRootNode();
		return List.of();
	}

	@Override
	public MenuNodeResponse getNodeById(Long id) {
		return null;
	}

	@Override
	public boolean createMenuNode(MenuNodeRequest menuNodeRequest) {
		return false;
	}

	@Override
	public boolean updateMenuNode(MenuNodeRequest menuNodeRequest, Long id) {
		return false;
	}

	@Override
	public boolean deleteMenuNode(Long id) {
		return false;
	}
}
