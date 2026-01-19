package com.l7bug.system.dao.jpa;

import com.l7bug.system.dao.dataobject.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RoleRepository
 *
 * @author Administrator
 * @since 2026/1/19 11:44
 */
@Repository
public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {
	Optional<SystemRole> findByFullCode(String fullCode);
}
