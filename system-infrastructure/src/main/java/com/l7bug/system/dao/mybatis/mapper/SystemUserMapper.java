package com.l7bug.system.dao.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.l7bug.system.dao.dataobject.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * SystemUserMapper
 *
 * @author Administrator
 * @since 2025/11/7 11:51
 */
@Repository
@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUser> {
}
