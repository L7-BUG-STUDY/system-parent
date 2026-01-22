package com.l7bug.system.gateway;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.l7bug.system.dao.dataobject.SystemRoleDo;
import com.l7bug.system.dao.jpa.SystemRoleRepository;
import com.l7bug.system.dao.mapstruct.RoleDoMapstruct;
import com.l7bug.system.dao.mybatis.mapper.SystemRoleMapper;
import com.l7bug.system.domain.role.Role;
import com.l7bug.system.domain.role.RoleGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RoleGatewayImpl
 *
 * @author Administrator
 * @since 2026/1/19 11:38
 */
@Slf4j
@AllArgsConstructor
@Component
public class RoleGatewayImpl implements RoleGateway {
	private final RoleDoMapstruct roleDoMapstruct;
	private final SystemRoleRepository systemRoleRepository;
	private final SystemRoleMapper systemRoleMapper;

	@Override
	public boolean save(Role role) {
		SystemRoleDo save = systemRoleRepository.save(roleDoMapstruct.mapDo(role));
		role.setId(save.getId());
		return true;
	}

	@Override
	public boolean save(Collection<Role> roles) {
		if (roles.isEmpty()) {
			return false;
		}
		Map<SystemRoleDo, Role> identityHashMap = roles.parallelStream().collect(Collectors.toMap(roleDoMapstruct::mapDo, item -> item, (old, newItem) -> newItem, IdentityHashMap::new));
		this.systemRoleRepository.saveAllAndFlush(identityHashMap.keySet());
		identityHashMap.entrySet().parallelStream().forEach(entry -> entry.getValue().setId(entry.getKey().getId()));
		return true;
	}

	@Override
	public List<Role> findLikeRightFullId(String fullId) {
		List<SystemRoleDo> systemRoleDos = systemRoleMapper.selectList(
			Wrappers.lambdaQuery(SystemRoleDo.class)
				.likeRight(StrUtil.isNotBlank(fullId), SystemRoleDo::getFullId, fullId)
		);
		return systemRoleDos.stream().map(roleDoMapstruct::mapDomain).toList();
	}

	@Override
	public Optional<Role> findById(@Nullable Long id) {
		if (id == null) {
			return Optional.empty();
		}
		Optional<SystemRoleDo> byId = this.systemRoleRepository.findById(id);
		return byId.map(roleDoMapstruct::mapDomain);
	}

	@Override
	public List<Role> findByFatherId(@Nullable Long fatherId) {
		return this.systemRoleRepository.findByFatherId(fatherId).parallelStream().map(roleDoMapstruct::mapDomain).toList();
	}

	@Override
	public void deleteById(@Nullable Long id) {
		if (id == null) {
			return;
		}
		this.systemRoleRepository.deleteById(id);
	}
}
