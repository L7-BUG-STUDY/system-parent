package com.l7bug.system.domain.role;

import jakarta.validation.Valid;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * RoleGatewayTestImpl
 *
 * @author Administrator
 * @since 2026/1/22 10:22
 */
@NullMarked
public class RoleGatewayTestImpl implements RoleGateway {
	private final Map<Long, Role> roleTable = new HashMap<>();
	private final Map<Long, List<Long>> menuTable = new HashMap<>();

	@Override
	public boolean save(Role role) {
		return this.save(Collections.singletonList(role));
	}

	@Override
	public boolean save(Collection<@Valid Role> roles) {
		for (Role item : roles) {
			if (item.getId() == null) {
				item.setId((long) UUID.randomUUID().hashCode());
			}
			roleTable.put(item.getId(), item);
		}
		return true;
	}

	@Override
	public boolean assignMenus(Long roleId, Collection<Long> menuIds) {
		menuTable.getOrDefault(roleId, new LinkedList<>()).addAll(menuIds);
		return true;
	}

	@Override
	public List<Long> findMenuIds(Long roleId) {
		return menuTable.getOrDefault(roleId, new LinkedList<>());
	}

	@Override
	public List<Role> findLikeRightFullId(String fullId) {
		return roleTable.values().parallelStream().filter(item -> item.getFullId().startsWith(fullId)).toList();
	}

	@Override
	public Optional<Role> findById(@Nullable Long id) {
		return Optional.ofNullable(roleTable.get(id));
	}

	@Override
	public List<Role> findByFatherId(@Nullable Long fatherId) {
		return roleTable.values().parallelStream().filter(item -> item.getFatherId().equals(fatherId)).toList();
	}

	@Override
	public void deleteById(@Nullable Long id) {
		roleTable.remove(id);
	}

	@Override
	public void deleteMenusByRoleId(Long roleId) {
		menuTable.remove(roleId);
	}
}
