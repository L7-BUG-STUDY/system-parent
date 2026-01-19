package com.l7bug.system.gateway;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.domain.role.Role;
import com.l7bug.system.domain.role.RoleGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * RoleGatewayImpl
 *
 * @author Administrator
 * @since 2026/1/19 11:38
 */
@Component
public class RoleGatewayImpl implements RoleGateway {
	@Override
	public boolean save(Role role) {
		return false;
	}

	@Override
	public List<Menu> findLikeFullCode(String fullCode) {
		return List.of();
	}

	@Override
	public Optional<Role> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public Optional<Role> findByCode(String code) {
		return Optional.empty();
	}

	@Override
	public boolean deleteById(Long id) {
		return false;
	}
}
