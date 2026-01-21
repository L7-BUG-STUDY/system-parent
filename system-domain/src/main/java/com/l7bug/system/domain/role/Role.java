package com.l7bug.system.domain.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

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
	 * 角色名称
	 */
	@NotBlank(message = "角色名称不能为空")
	private String name;

	@NotNull(message = "父节点id不能为空")
	private Long fatherId;
	@NotBlank(message = "全路径id不能为空")
	private String fullId;
	/**
	 * 角色状态
	 */
	@NotNull(message = "角色状态不能为空")
	private RoleStatus status = RoleStatus.ENABLED;

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

	/**
	 * 保存角色信息，包括构建角色的全路径编码、验证唯一性以及更新子节点的路径信息
	 * <p>
	 * 此方法会执行以下操作：
	 * 1. 根据父级角色编码构建当前角色的全路径编码
	 * 2. 检查具有相同全路径编码的角色是否存在，确保路径唯一性
	 * 3. 如果角色的父级路径发生变化，则同步更新所有子节点的全路径编码
	 * 4. 保存当前角色信息到数据库
	 */
	public void save() {
		// 查找父级角色，用于构建当前角色的全路径编码
		// Optional<Role> father = this.roleGateway.findByFullCode(this.getFatherFullCode());
		// father.ifPresent(role -> {
		// 	this.setFullCode(role.getFullCode() + PATH_SEPARATOR + this.getCode());
		// });
		// // 根据是否有父级角色来确定当前角色的全路径编码格式
		// father.ifPresentOrElse(role -> {
		// 	this.setFullCode(role.getFullCode() + PATH_SEPARATOR + this.getCode());
		// }, () -> {
		// 	this.setFullCode(PATH_SEPARATOR + this.getCode());
		// });
		// // 获取当前角色的旧数据，用于比较父级路径是否发生变化
		// Optional<Role> oldData = this.roleGateway.findById(this.getId());
		// // 检查是否有其他角色已使用相同的全路径编码
		// Optional<Role> byFullCode = this.roleGateway.findByFullCode(this.getFullCode());
		// byFullCode.ifPresent(role -> {
		// 	if (!Optional.ofNullable(role.getId()).orElse(Long.MIN_VALUE).equals(this.getId())) {
		// 		// 如果存在其他角色使用相同路径编码，则抛出异常
		// 		throw new ClientException(ClientErrorCode.NODE_IS_NOT_NULL, this.getFullCode() + "::" + ClientErrorCode.NODE_IS_NOT_NULL.getMessage());
		// 	}
		// });
		// // 如果角色的父级路径发生变化，则更新所有子节点的路径信息
		// oldData.ifPresent(role -> {
		// 	if (!role.getFatherFullCode().equals(this.getFatherFullCode())) {
		// 		// 同步更新子节点的全路径编码
		// 		String oldFullCode = role.getFullCode() + PATH_SEPARATOR;
		// 		List<Role> oldLikeFullCode = this.roleGateway.findLikeFullCode(oldFullCode);
		// 		oldLikeFullCode.parallelStream()
		// 			.forEach(item -> {
		// 				item.setFullCode(item.getFullCode().replace(oldFullCode, this.getFullCode() + PATH_SEPARATOR));
		// 				item.setFatherFullCode(item.getFatherFullCode().replace(oldFullCode, this.getFullCode() + PATH_SEPARATOR));
		// 			});
		// 		this.roleGateway.save(oldLikeFullCode);
		// 	}
		// });
		// // 保存当前角色信息到数据库
		// this.roleGateway.save(this);
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
