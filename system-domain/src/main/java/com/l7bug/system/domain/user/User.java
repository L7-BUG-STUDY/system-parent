package com.l7bug.system.domain.user;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import lombok.Data;

import java.util.Collection;

/**
 * User
 *
 * @author Administrator
 * @since 2025/11/7 10:35
 */
@Data
public class User {
	private static final int WAIT_TIME = 1;
	private UserGateway userGateway;
	private Long id;
	private String username;
	private String nickname;
	private String password;
	/**
	 * 密码,明文
	 */
	private transient String rawPassword;
	private transient Collection<String> authoritiesSet;
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
			throw new ClientException(ClientErrorCode.USER_NOT_NULL);
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
