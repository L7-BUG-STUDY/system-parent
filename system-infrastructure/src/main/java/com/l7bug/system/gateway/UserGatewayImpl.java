package com.l7bug.system.gateway;

import com.l7bug.system.convertor.UserConvertor;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import com.l7bug.system.mybatis.service.SystemUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * UserGatewayImpl
 *
 * @author Administrator
 * @since 2025/11/7 10:56
 */
@AllArgsConstructor
@Component
public class UserGatewayImpl implements UserGateway {
    private final SystemUserService systemUserService;
    private final UserConvertor userConvertor;

    @Override
    public boolean save(User user) {
        SystemUser systemUser = userConvertor.mapDo(user);
        boolean flag = this.systemUserService.saveOrUpdate(systemUser);
        user.setId(systemUser.getId());
        return flag;
    }

    @Override
    public User getUserByUsername(String username) {
        SystemUser systemUser = systemUserService.findByUsername(username);
        if (systemUser == null) {
            return null;
        }
        return userConvertor.mapDomain(systemUser);
    }
}
