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
	private UserGateway userGateway;
	private Long id;
	private String username;
	private String nickname;
	private String password;
	/**
	 * 密码,明文
	 */
	private transient String rawPassword;
	private Status status;

	public User(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

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

	public boolean save() {
		User userByUsername = userGateway.getUserByUsername(username);
		if (userByUsername != null && !userByUsername.getId().equals(id)) {
			return false;
		}
		if (this.rawPassword != null) {
			this.password = userGateway.encode(this.rawPassword);
		}
		return userGateway.save(this);
	}

	public String login() {
		return this.userGateway.login(this.username, this.rawPassword);
	}
}
