package com.l7bug.system.dao.jpa;

import com.l7bug.system.dao.dataobject.SystemRoleMenuDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * SystemRoleMenuRepository
 *
 * @author Administrator
 * @since 2026/1/22 17:53
 */
public interface SystemRoleMenuRepository extends JpaRepository<SystemRoleMenuDo, Long> {
	List<SystemRoleMenuDo> findByRoleId(Long roleId);

	void removeAllByRoleId(Long roleId);
}
