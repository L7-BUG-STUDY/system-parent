package com.l7bug.system.domain.role;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * Role
 *
 * @author Administrator
 * @since 2025/11/13 11:48
 */
@Data
public class Role {
	@Getter(AccessLevel.PRIVATE)
	private final RoleGateway roleGateway;
	private Long id;

	/**
	 * 上级id
	 */
	private String fatherId;

	/**
	 * 角色标识
	 */
	@NotBlank(message = "角色编码不能为空")
	private String code;

	/**
	 * 名称
	 */
	@NotBlank(message = "名称不能为空")
	private String name;

	/**
	 * 全路径id
	 */
	private String fullId;

	/**
	 * 全路径名称
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
		Role oldData = this.getRoleGateway().get(this.getCode());
		if (oldData != null && !oldData.getId().equals(this.getId())) {
			throw new ClientException(ClientErrorCode.ROLE_CODE_NOT_NULL);
		}
		boolean canUpdateFullValue = false;
		String oldFatherId = Optional.ofNullable(oldData).map(Role::getFatherId).orElse("");
		String oldName = Optional.ofNullable(oldData).map(Role::getName).orElse("");
		if (!oldFatherId.equals(this.fatherId)) {
			// 父节点变了,所有相关节点都需要修改全路径
			canUpdateFullValue = true;
		}
		if (!oldName.equals(this.name)) {
			// 名称变了,所有相关节点也要调整
			canUpdateFullValue = true;
		}
		List<Role> allChildren = this.getRoleGateway().getAllChildren(this.getId());
		if (canUpdateFullValue) {
		}
		for (Role children : allChildren) {
			// 修改所有子节点的全路径信息
			String childrenFullId = children.getFullId();
		}
		return true;
	}
}
