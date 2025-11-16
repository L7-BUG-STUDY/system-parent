package com.l7bug.system.domain.role;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Role
 *
 * @author Administrator
 * @since 2025/11/13 11:48
 */
@Data
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Role {
	@Getter(AccessLevel.PRIVATE)
	private final RoleGateway roleGateway;
	private Long id;

	/**
	 * 上级id
	 */
	private String fatherCode;

	/**
	 * 角色标识
	 */
	@NotBlank(message = "角色编码不能为空")
	private String code;

	/**
	 * 全路径名称
	 */
	private String fullCode;

	/**
	 * 名称
	 */
	@NotBlank(message = "名称不能为空")
	private String name;

	/**
	 *
	 */
	private String fullName;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 状态
	 */
	private RoleStatus status;

	private Collection<Role> collection;


	public boolean isEnable() {
		return status == RoleStatus.ENABLE;
	}

	public boolean isDisable() {
		return status == RoleStatus.DISABLE;
	}

	public void setDisable() {
		this.status = RoleStatus.DISABLE;
	}

	public void setEnable() {
		this.status = RoleStatus.ENABLE;
	}

	/**
	 * 保存
	 *
	 * @return true 成功修改
	 */
	public boolean save() {
		Role role = this.getRoleGateway().get(this.getCode());
		if (role != null && !role.getId().equals(this.getId())) {
			throw new ClientException(ClientErrorCode.ROLE_CODE_NOT_NULL);
		}
		boolean canUpdateChildren = false;
		String oldCode = Optional.ofNullable(role).map(Role::getCode).orElse("");
		String oldName = Optional.ofNullable(role).map(Role::getName).orElse("");
		if (!oldCode.equals(this.code)) {
			canUpdateChildren = true;
		}
		if (!oldName.equals(this.name)) {
			canUpdateChildren = true;
		}

		return true;
	}

	public void queryAllChildren() {
		List<Role> allChildren = this.getRoleGateway().getAllChildren(this.id);
		if (allChildren == null) {
			return;
		}
	}
}
