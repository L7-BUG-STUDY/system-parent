package com.l7bug.system.domain.user;

import com.google.common.base.Strings;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * User
 *
 * @author Administrator
 * @since 2025/11/7 10:35
 */
@Data
public class User {
	@Getter(AccessLevel.PRIVATE)
	private final UserGateway userGateway;
	private Long id;
	private String username;
	private String nickname;
	private String password;
	/**
	 * 密码,明文
	 */
	private transient String rawPassword;
	private transient Collection<String> authoritiesSet;
	private UserStatus status;

	private LocalDateTime createTime;

	public User(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public boolean isEnable() {
		return status == UserStatus.ENABLE;
	}

	public boolean isDisable() {
		return status == UserStatus.DISABLE;
	}

	public void setDisable() {
		this.status = UserStatus.DISABLE;
	}

	public void setEnable() {
		this.status = UserStatus.ENABLE;
	}

	/**
	 * 保存用户信息
	 * 
	 * 该方法用于保存当前用户对象的信息到数据库中。在保存之前会进行以下操作：
	 * 1. 检查用户名是否已经存在，如果存在且不是当前用户则抛出异常
	 * 2. 如果原始密码不为空，则对密码进行加密处理
	 * 3. 调用网关层保存用户信息
	 * 
	 * @return boolean 保存成功返回true，失败返回false
	 * @throws ClientException 当用户名已存在时抛出此异常
	 */
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

	public boolean checkPassword(String oldPassword) {
		if (Strings.isNullOrEmpty(oldPassword)) {
			return false;
		}
		User userById = this.userGateway.getUserById(this.getId());
		if (userById == null) {
			return false;
		}
		return this.userGateway.matches(this.userGateway.encode(oldPassword), userById.getPassword());
	}

	public boolean delete() {
		return this.userGateway.deleteById(this.getId());
	}

}
