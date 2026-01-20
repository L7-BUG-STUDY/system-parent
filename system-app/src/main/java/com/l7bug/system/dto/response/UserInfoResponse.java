package com.l7bug.system.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * UserResponse
 *
 * @author Administrator
 * @since 2025/11/14 16:26
 */
@Data
public class UserInfoResponse {
	private Long id;
	private String username;
	private String nickname;
	private Integer status;
	private LocalDateTime createTime;
}
