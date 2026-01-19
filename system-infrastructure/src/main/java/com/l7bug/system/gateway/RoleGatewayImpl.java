package com.l7bug.system.gateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.l7bug.system.dao.dataobject.SystemRole;
import com.l7bug.system.dao.jpa.SystemRoleRepository;
import com.l7bug.system.dao.mapstruct.RoleDoMapstruct;
import com.l7bug.system.dao.mybatis.mapper.SystemRoleMapper;
import com.l7bug.system.domain.role.Role;
import com.l7bug.system.domain.role.RoleGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * RoleGatewayImpl
 *
 * @author Administrator
 * @since 2026/1/19 11:38
 */
@AllArgsConstructor
@Component
public class RoleGatewayImpl implements RoleGateway {
	private final RoleDoMapstruct roleDoMapstruct;
	private final SystemRoleRepository systemRoleRepository;
	private final SystemRoleMapper systemRoleMapper;

	@Override
	public boolean save(Role role) {
		SystemRole save = systemRoleRepository.save(roleDoMapstruct.mapDo(role));
		role.setId(save.getId());
		return true;
	}

	@Override
	public List<Role> findLikeFullCode(String fullCode) {
		List<SystemRole> systemRoles = systemRoleMapper.selectList(Wrappers.lambdaQuery(SystemRole.class).likeRight(SystemRole::getFullCode, fullCode));
		return systemRoles.stream().map(roleDoMapstruct::mapDomain).toList();
	}

	@Override
	public Optional<Role> findById(Long id) {
		Optional<SystemRole> byId = this.systemRoleRepository.findById(id);
		return byId.map(roleDoMapstruct::mapDomain);
	}

	@Override
	public boolean deleteById(Long id) {
		this.systemRoleRepository.deleteById(id);
		return true;
	}
}
