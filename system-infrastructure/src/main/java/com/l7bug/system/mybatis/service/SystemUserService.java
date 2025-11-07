package com.l7bug.system.mybatis.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import com.l7bug.system.mybatis.mapper.SystemUserMapper;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * SystemUserService
 *
 * @author Administrator
 * @since 2025/11/7 11:58
 */
@Service
@Validated
public class SystemUserService extends ServiceImpl<SystemUserMapper, SystemUser> {
    public SystemUser findByUsername(@NotBlank(message = "用户名不能为空") String username) {
        return this.baseMapper.selectOne(Wrappers.lambdaQuery(SystemUser.class).eq(SystemUser::getUsername, username));
    }
}
