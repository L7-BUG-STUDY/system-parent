package com.l7bug.system.domain.role;

import com.l7bug.system.domain.menu.Menu;
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

	List<Menu> findLikeFullCode(String fullCode);

	Optional<Role> findById(Long id);

	Optional<Role> findByCode(String code);

	boolean deleteById(Long id);
}
