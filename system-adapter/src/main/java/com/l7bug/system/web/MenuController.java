package com.l7bug.system.web;

import com.l7bug.common.result.Result;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
import com.l7bug.system.service.MenuAppService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * MenuController
 *
 * @author Administrator
 * @since 2025/12/9 10:43
 */
@RestController
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController {
	private final MenuAppService menuAppService;

	@GetMapping("/root")
	public Result<MenuNodeResponse> getRoot() {
		return menuAppService.getRootNode();
	}

	@GetMapping("/{id}")
	public Result<MenuNodeResponse> getById(@PathVariable Long id) {
		return menuAppService.getNodeById(id);
	}

	@PostMapping
	public Result<MenuNodeResponse> createMenuNode(@Valid @RequestBody MenuNodeRequest menuNodeRequest) {
		return menuAppService.createMenuNode(menuNodeRequest);
	}

	@PutMapping("/{id}")
	public Result<Boolean> updateMenuNode(@PathVariable Long id, @RequestBody MenuNodeRequest menuNodeRequest) {
		return menuAppService.updateMenuNode(menuNodeRequest, id);
	}


	@DeleteMapping("/{id}")
	public Result<Boolean> deleteNode(@PathVariable Long id) {
		return menuAppService.deleteMenuNode(id);
	}

	/**
	 * 修改指定菜单节点的排序值
	 *
	 * @param id   菜单节点ID
	 * @param sort 排序值增量
	 * @return 操作结果，true表示操作成功，false表示操作失败
	 */
	@PutMapping("/{id}/sort/{sort}")
	public Result<Boolean> addSortVal(@PathVariable Long id, @PathVariable Integer sort) {
		return menuAppService.addSortVal(id, sort);
	}
}
