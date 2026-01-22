package com.l7bug.system.domain.role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * RoleGateway
 *
 * @author Administrator
 * @since 2026/1/19 10:19
 */
@Validated
public interface RoleGateway {
	/**
	 * 保存角色信息
	 *
	 * @param role 角色
	 * @return 保存结果
	 */
	boolean save(@Valid Role role);

	/**
	 * 保存角色信息
	 *
	 * @param roles 角色
	 * @return 保存结果
	 */
	boolean save(@NotNull Collection<@Valid Role> roles);

	/**
	 * 为角色分配菜单
	 *
	 * @param roleId  角色id
	 * @param menuIds 菜单id
	 * @return 分配结果
	 */
	boolean assignMenus(@NotNull Long roleId, @NotNull Collection<Long> menuIds);

	/**
	 * 查询角色拥有的菜单
	 *
	 * @param roleId 角色id
	 * @return 菜单id
	 */
	List<Long> findMenuIds(Long roleId);

	List<Role> findLikeRightFullId(String fullId);

	Optional<Role> findById(@Nullable Long id);

	List<Role> findByFatherId(@Nullable Long fatherId);

	void deleteById(@Nullable Long id);

	/**
	 * 删除角色的所有菜单
	 *
	 * @param roleId 角色id
	 */
	void deleteMenusByRoleId(Long roleId);
}
