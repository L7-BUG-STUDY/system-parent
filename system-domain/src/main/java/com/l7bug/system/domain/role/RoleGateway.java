package com.l7bug.system.domain.role;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

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
	boolean save(@Valid Role role);

	List<Role> findLikeFullCode(String fullCode);

	Optional<Role> findById(Long id);

	boolean deleteById(Long id);
}
