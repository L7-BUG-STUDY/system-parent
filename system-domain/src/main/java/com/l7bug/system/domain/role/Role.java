package com.l7bug.system.domain.role;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Role
 *
 * @author Administrator
 * @since 2026/1/16 18:16
 */
@Data
public class Role implements Comparable<Role> {
	public static final String PATH_SEPARATOR = "/";
	public static final Long ROOT_ID = -1L;
	@Getter(AccessLevel.PRIVATE)
	private final RoleGateway roleGateway;
	@Nullable
	private Long id;
	/**
	 * 角色名称
	 */
	@NotBlank(message = "角色名称不能为空")
	private String name = "";

	@NotNull(message = "父节点id不能为空")
	private Long fatherId;
	@NotBlank(message = "全路径id不能为空")
	private String fullId = "";
	/**
	 * 角色状态
	 */
	@NotNull(message = "角色状态不能为空")
	private RoleStatus status = RoleStatus.ENABLED;

	private Integer sort = 0;
	private String remark = "";
	private PriorityQueue<Role> children = new PriorityQueue<>();

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
		boolean isNewData = this.getId() == null;
		if (isNewData) {
			// 先占位,稍后会处理
			this.setFullId(PATH_SEPARATOR + UUID.randomUUID());
			this.roleGateway.save(this);
		}
		// 查找父级角色，用于构建当前角色的全路径编码
		Optional<Role> father = this.roleGateway.findById(this.getFatherId());
		father.ifPresentOrElse(
			role -> this.setFullId(role.getFullId() + PATH_SEPARATOR + this.getId()),
			() -> {
				this.setFullId(PATH_SEPARATOR + this.getId());
				this.setFatherId(ROOT_ID);
			}
		);
		if (isNewData) {
			this.roleGateway.save(this);
			return;
		}
		Optional<Role> oldData = this.roleGateway.findById(this.getId());

		oldData.ifPresent(role -> {
			if (!role.getFatherId().equals(this.getFatherId())) {
				this.moveFather(this.getFatherId());
			} else {
				this.roleGateway.save(this);
			}
		});
		this.refreshSiblingSortValue();
	}

	public void moveFather(Long fatherId) {
		Optional<Role> father = this.roleGateway.findById(fatherId);
		this.setFatherId(father.map(Role::getId).orElse(ROOT_ID));
		this.setFullId(father.map(Role::getFullId).orElse("") + PATH_SEPARATOR + this.getId());
		this.findAllChildren();
		this.updateAllChildrenFullId();
		Collection<Role> roles = this.flattenAllChildrenNode();
		this.roleGateway.save(roles);
		this.roleGateway.save(this);
	}

	public void findAllChildren() {
		if (this.getId() == null) {
			this.setChildren(new PriorityQueue<>());
			return;
		}
		List<Role> roles;
		if (this.getId().equals(ROOT_ID)) {
			roles = this.getRoleGateway().findLikeRightFullId("");
		} else {
			Optional<Role> byId = this.roleGateway.findById(this.getId());
			roles = byId.map(item -> this.roleGateway.findLikeRightFullId(item.getFullId() + PATH_SEPARATOR)).orElse(List.of());
		}
		if (roles.isEmpty()) {
			this.setChildren(new PriorityQueue<>());
			return;
		}
		var fatherIdMap = roles.parallelStream().collect(Collectors.groupingBy(Role::getFatherId));
		for (Role role : roles) {
			role.setChildren(new PriorityQueue<>(fatherIdMap.getOrDefault(role.getId(), new LinkedList<>())));
		}
		this.setChildren(new PriorityQueue<>(fatherIdMap.getOrDefault(this.getId(), new LinkedList<>())));
	}

	public void updateAllChildrenFullId() {
		for (Role child : this.getChildren()) {
			child.setFullId(this.getFullId() + PATH_SEPARATOR + child.getId());
			child.updateAllChildrenFullId();
		}
	}

	public Collection<Role> flattenAllChildrenNode() {
		var roles = new LinkedList<Role>();
		for (Role role : this.children) {
			roles.add(role);
			roles.addAll(role.flattenAllChildrenNode());
		}
		return roles;
	}

	public void delete() {
		if (this.id == null) {
			return;
		}
		this.findAllChildren();
		if (!this.getChildren().isEmpty()) {
			throw new ClientException(ClientErrorCode.CHILDREN_IS_NOT_NULL);
		}
		this.roleGateway.deleteById(this.id);
	}

	public void refreshSiblingSortValue() {
		List<Role> byFatherId = this.roleGateway.findByFatherId(this.getFatherId());
		if (byFatherId.isEmpty()) {
			return;
		}
		PriorityQueue<Role> roles = new PriorityQueue<>(byFatherId);
		int sort = 0;
		while (!roles.isEmpty()) {
			roles.poll().setSort(sort);
			sort += 2;
		}
		this.roleGateway.save(byFatherId);
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
