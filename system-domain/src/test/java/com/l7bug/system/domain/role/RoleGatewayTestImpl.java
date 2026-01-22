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
	private final Map<Long, Role> map = new HashMap<>();

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
			map.put(item.getId(), item);
		}
		return true;
	}

	@Override
	public List<Role> findLikeRightFullId(String fullId) {
		return map.values().parallelStream().filter(item -> item.getFullId().startsWith(fullId)).toList();
	}

	@Override
	public Optional<Role> findById(@Nullable Long id) {
		return Optional.ofNullable(map.get(id));
	}

	@Override
	public void deleteById(@Nullable Long id) {
		map.remove(id);
	}
}
