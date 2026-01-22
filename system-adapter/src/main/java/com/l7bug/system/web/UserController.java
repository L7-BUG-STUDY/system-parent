package com.l7bug.system.web;

import com.l7bug.common.page.PageData;
import com.l7bug.common.result.Result;
import com.l7bug.system.dto.request.QueryUserRequest;
import com.l7bug.system.dto.request.UpdateUserRequest;
import com.l7bug.system.dto.response.UserInfoResponse;
import com.l7bug.system.service.UserAppService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * @author Administrator
 * @since 2025/11/14 15:20
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
	private final UserAppService userAppService;

	@GetMapping
	public Result<PageData<UserInfoResponse>> page(QueryUserRequest queryUserRequest) {
		return userAppService.pageUser(queryUserRequest);
	}

	@PutMapping("/{id}")
	public Result<Void> update(@PathVariable("id") Long id, @RequestBody UpdateUserRequest updateUserRequest) {
		return userAppService.updateUserById(id, updateUserRequest);
	}

	@PostMapping
	public Result<Void> create(@RequestBody UpdateUserRequest updateUserRequest) {
		return userAppService.createUser(updateUserRequest);
	}

	@DeleteMapping("/{id}")
	public Result<Void> delete(@PathVariable Long id) {
		return userAppService.deleteUserById(id);
	}

}
