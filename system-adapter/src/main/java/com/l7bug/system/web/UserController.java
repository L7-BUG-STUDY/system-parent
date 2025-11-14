package com.l7bug.system.web;

import com.l7bug.common.page.PageData;
import com.l7bug.common.result.Result;
import com.l7bug.system.client.UserClient;
import com.l7bug.system.dto.request.QueryUserRequest;
import com.l7bug.system.dto.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	private final UserClient userClient;

	@GetMapping
	public Result<PageData<UserInfoResponse>> page(QueryUserRequest queryUserRequest) {
		return userClient.pageUser(queryUserRequest);
	}
}
