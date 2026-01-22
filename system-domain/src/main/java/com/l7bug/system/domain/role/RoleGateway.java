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
	boolean save(@Valid Role role);

	boolean save(@NotNull Collection<@Valid Role> roles);

	List<Role> findLikeRightFullId(String fullId);

	Optional<Role> findById(@Nullable Long id);

	List<Role> findByFatherId(@Nullable Long fatherId);

	void deleteById(@Nullable Long id);
}
