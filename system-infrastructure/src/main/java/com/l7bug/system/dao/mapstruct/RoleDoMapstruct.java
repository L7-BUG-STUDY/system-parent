package com.l7bug.system.dao.mapstruct;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.context.ApplicationContext;

/**
 * RoleDoMapstruct
 *
 * @author Administrator
 * @since 2026/1/19 11:36
 */
@Slf4j
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class RoleDoMapstruct {
	@Resource
	private ApplicationContext applicationContext;

}
