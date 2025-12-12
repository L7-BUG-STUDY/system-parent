package com.l7bug.system.client;

import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.menu.MenuGateway;
import com.l7bug.system.domain.menu.MenuType;
import com.l7bug.system.domain.menu.MetaVal;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
import com.l7bug.system.mapstruct.MenuAppMapstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

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
	private final MenuAppMapstruct menuAppMapstruct;

	@Override
	public Result<MenuNodeResponse> getRootNode() {
		Menu menu = menuAppMapstruct.menu();
		menu.setId(-1L);
		menu.setFullId("");
		menu.setName("root");
		menu.findChildren();
		MetaVal meta = new MetaVal();
		menu.setMeta(meta);
		menu.setType(MenuType.FOLDER);
		meta.setTitle(Map.of(MetaVal.ZH_CN, "根节点", MetaVal.EN_US, "rootNode"));
		meta.setIcon("root-list");
		return Results.success(menuAppMapstruct.mapResponse(menu));
	}

	@Override
	public Result<MenuNodeResponse> getNodeById(Long id) {
		return Results.success(menuAppMapstruct.mapResponse(menuGateway.findById(id)));
	}

	@Override
	public Result<MenuNodeResponse> createMenuNode(MenuNodeRequest menuNodeRequest) {
		Menu menu = menuAppMapstruct.mapDomain(menuNodeRequest);
		menu.save();
		return Results.success(menuAppMapstruct.mapResponse(menu));
	}

	@Override
	public Result<Boolean> updateMenuNode(MenuNodeRequest menuNodeRequest, Long id) {
		Menu byId = menuGateway.findById(id);
		if (byId == null) {
			return Results.success(false);
		}
		Menu menu = menuAppMapstruct.mapDomain(menuNodeRequest);
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
