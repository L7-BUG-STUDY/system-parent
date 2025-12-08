package com.l7bug.system.client;

import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.system.convertor.MenuConvertor;
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
	private final MenuConvertor menuConvertor;

	@Override
	public Result<List<MenuNodeResponse>> getAllRootNodes() {
		List<Menu> allRootNode = menuGateway.findAllRootNode();
		return Results.success(menuConvertor.mapResponseByColletion(allRootNode));
	}

	@Override
	public Result<MenuNodeResponse> getNodeById(Long id) {
		return Results.success(menuConvertor.mapResponse(menuGateway.findById(id)));
	}

	@Override
	public Result<MenuNodeResponse> createMenuNode(MenuNodeRequest menuNodeRequest) {
		Menu menu = menuConvertor.mapDomain(menuNodeRequest);
		menu.save();
		return Results.success(menuConvertor.mapResponse(menu));
	}

	@Override
	public Result<Boolean> updateMenuNode(MenuNodeRequest menuNodeRequest, Long id) {
		Menu byId = menuGateway.findById(id);
		if (byId == null) {
			return Results.success(false);
		}
		Menu menu = menuConvertor.mapDomain(menuNodeRequest);
		menu.setId(id);
		menu.save();
		return Results.success(true);
	}

	@Override
	public Result<Boolean> deleteMenuNode(Long id) {
		Menu byId = menuGateway.findById(id);
		if (byId == null) {
			return Results.success(false);
		}
		return Results.success(byId.delete());
	}
}
