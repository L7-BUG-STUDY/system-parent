package com.l7bug.system.service.impl;

import com.l7bug.system.domain.role.RoleGateway;
import com.l7bug.system.service.RoleAppService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * RoleAppServiceImpl
 *
 * @author Administrator
 * @since 2026/1/22 11:06
 */
@AllArgsConstructor
@Service
public class RoleAppServiceImpl implements RoleAppService {
	private final RoleGateway roleGateway;
}
