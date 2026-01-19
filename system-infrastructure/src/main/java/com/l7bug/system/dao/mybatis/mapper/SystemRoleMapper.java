package com.l7bug.system.dao.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.l7bug.system.dao.dataobject.SystemRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * SystemRoleMapper
 *
 * @author Administrator
 * @since 2026/1/19 11:50
 */
@Repository
@Mapper
public interface SystemRoleMapper extends BaseMapper<SystemRole> {
}
