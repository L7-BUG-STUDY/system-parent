package com.l7bug.system.domain.user;

import lombok.Data;

/**
 * User
 *
 * @author Administrator
 * @since 2025/11/7 10:35
 */
@Data
public class User {
	private Long id;
	private String username;
	private String nickname;
	private String password;
	private Status status;

	public boolean isEnable() {
		return status == Status.ENABLE;
	}

	public boolean isDisable() {
		return status == Status.DISABLE;
	}

	public void setDisable() {
		this.status = Status.DISABLE;
	}

	public void setEnable() {
		this.status = Status.ENABLE;
	}

	public boolean save(UserGateway userGateway) {
		User userByUsername = userGateway.getUserByUsername(this.username);
		if (userByUsername != null && !userByUsername.getId().equals(this.id)) {
			return false;
		}
		return userGateway.save(this);
	}
}
