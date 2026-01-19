package com.l7bug.system.domain.role;

import jakarta.validation.Valid;

import java.util.Optional;

/**
 * RoleGateway
 *
 * @author Administrator
 * @since 2026/1/19 10:19
 */
public interface RoleGateway {
	boolean save(@Valid Role role);

	Optional<Role> findById(Long id);

	Optional<Role> findByCode(String code);

	boolean deleteById(Long id);
}
