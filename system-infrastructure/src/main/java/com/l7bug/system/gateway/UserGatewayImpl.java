package com.l7bug.system.gateway;

import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;

/**
 * UserGatewayImpl
 *
 * @author Administrator
 * @since 2025/11/7 10:56
 */
public class UserGatewayImpl implements UserGateway {
    @Override
    public boolean save(User user) {
        return false;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }
}
