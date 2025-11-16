package com.l7bug.system.gateway;

import com.l7bug.system.domain.role.Role;
import com.l7bug.system.domain.role.RoleGateway;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * RoleGatewayImpl
 *
 * @author Administrator
 * @since 2025/11/13 12:26
 */
@Component
public class RoleGatewayImpl implements RoleGateway {

	@Override
	public Role get(Long id) {
		return null;
	}

	@Override
	public Role get(String code) {
		return null;
	}

	@Override
	public List<Role> getAllChildren(Long id) {
		return List.of();
	}

	@Override
	public boolean save(Role role) {
		return false;
	}

	@Override
	public boolean updateChildren(Collection<Role> roles) {
		return false;
	}

	@Override
	public boolean delete(Long id) {
		return false;
	}
}
