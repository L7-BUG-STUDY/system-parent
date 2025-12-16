package com.l7bug.system.web;

import com.l7bug.common.result.Result;
import com.l7bug.system.client.MenuClient;
import com.l7bug.system.dto.request.MenuNodeRequest;
import com.l7bug.system.dto.response.MenuNodeResponse;
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
	private final MenuClient menuClient;

	@GetMapping("/root")
	public Result<MenuNodeResponse> getRoot() {
		return menuClient.getRootNode();
	}

	@GetMapping("/{id}")
	public Result<MenuNodeResponse> getById(@PathVariable Long id) {
		return menuClient.getNodeById(id);
	}

	@PostMapping
	public Result<MenuNodeResponse> createMenuNode(@Valid @RequestBody MenuNodeRequest menuNodeRequest) {
		return menuClient.createMenuNode(menuNodeRequest);
	}

	@PutMapping("/{id}")
	public Result<Boolean> updateMenuNode(@PathVariable Long id, @RequestBody MenuNodeRequest menuNodeRequest) {
		return menuClient.updateMenuNode(menuNodeRequest, id);
	}


	@DeleteMapping("/{id}")
	public Result<Boolean> deleteNode(@PathVariable Long id) {
		return menuClient.deleteMenuNode(id);
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
		return menuClient.addSortVal(id, sort);
	}
}
