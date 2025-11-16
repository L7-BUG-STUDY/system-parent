package com.l7bug.system.domain.role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * RoleGateway
 *
 * @author Administrator
 * @since 2025/11/13 12:19
 */
public interface RoleGateway {
	Role get(@NotNull(message = "id不能为空") Long id);

	Role get(@NotEmpty(message = "编码不能为空") String code);

	List<Role> getAllChildren(@NotNull(message = "id不能为空") Long id);

	boolean save(@NotNull(message = "角色不能为空") Role role);

	boolean updateChildren(Collection<Role> roles);

	boolean delete(Long id);


}
