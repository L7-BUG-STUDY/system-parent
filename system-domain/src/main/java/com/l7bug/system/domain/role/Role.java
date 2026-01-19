package com.l7bug.system.domain.role;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Role
 *
 * @author Administrator
 * @since 2026/1/16 18:16
 */
@Data
public class Role implements Comparable<Role> {
	public static final String PATH_SEPARATOR = "/";
	@Getter(AccessLevel.PRIVATE)
	private final RoleGateway roleGateway;
	@Nullable
	private Long id;
	/**
	 * 角色编码
	 */
	@NotBlank(message = "角色编码不能为空")
	private String code;
	/**
	 * 父级角色编码
	 */
	@NotBlank(message = "父级编码不能为空")
	private String fatherFullCode;
	/**
	 * 角色状态
	 */
	@NotNull(message = "角色状态不能为空")
	private RoleStatus status = RoleStatus.ENABLED;
	/**
	 * 角色名称
	 */
	@NotBlank(message = "角色名称不能为空")
	private String name;
	/**
	 * 全路径编码
	 */
	private String fullCode;

	private Integer sort = 0;
	private String remark;

	/**
	 * 将角色状态设置为禁用
	 * 此方法会将当前角色的status字段更新为RoleStatus.DISABLED
	 */
	public void disabled() {
		this.status = RoleStatus.DISABLED;
	}

	/**
	 * 将角色状态设置为启用
	 * 此方法会将当前角色的status字段更新为RoleStatus.ENABLED
	 */
	public void enabled() {
		this.status = RoleStatus.ENABLED;
	}

	public void save() {
		Optional<Role> father = this.roleGateway.findByFullCode(this.getFatherFullCode());
		father.ifPresent(role -> {
			this.setFullCode(role.getFullCode() + PATH_SEPARATOR + this.getCode());
		});
		father.ifPresentOrElse(role -> {
			this.setFullCode(role.getFullCode() + PATH_SEPARATOR + this.getCode());
		}, () -> {
			this.setFullCode(PATH_SEPARATOR + this.getCode());
		});
		Optional<Role> oldData = this.roleGateway.findById(this.getId());
		Optional<Role> byFullCode = this.roleGateway.findByFullCode(this.getFullCode());
		byFullCode.ifPresent(role -> {
			if (!Optional.ofNullable(role.getId()).orElse(Long.MIN_VALUE).equals(this.getId())) {
				throw new ClientException(ClientErrorCode.NODE_IS_NOT_NULL, this.getFullCode() + "::" + ClientErrorCode.NODE_IS_NOT_NULL.getMessage());
			}
		});
		oldData.ifPresent(role -> {
			if (!role.getFatherFullCode().equals(this.getFatherFullCode())) {
				// 同步更新子节点的全路径编码
				String oldFullCode = role.getFullCode() + PATH_SEPARATOR;
				List<Role> oldLikeFullCode = this.roleGateway.findLikeFullCode(oldFullCode);
				oldLikeFullCode.parallelStream()
					.forEach(item -> {
						item.setFullCode(item.getFullCode().replace(oldFullCode, this.getFullCode() + PATH_SEPARATOR));
						item.setFatherFullCode(item.getFatherFullCode().replace(oldFullCode, this.getFullCode() + PATH_SEPARATOR));
					});
				this.roleGateway.save(oldLikeFullCode);
			}
		});
		this.roleGateway.save(this);
	}

	public void delete() {
		if (this.id == null) {
			return;
		}
		this.roleGateway.deleteById(this.id);
	}

	@Override
	public int compareTo(Role o) {
		int compareTo = this.sort.compareTo(o.getSort());
		if (compareTo == 0) {
			if (o.getId() == null) {
				return -1;
			}
			if (this.getId() == null) {
				return 1;
			}
			return this.getId().compareTo(o.getId());
		}
		return compareTo;
	}
}
