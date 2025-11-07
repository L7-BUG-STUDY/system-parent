package com.l7bug.system.domain.user;

/**
 * UserGateway
 *
 * @author Administrator
 * @since 2025/11/7 10:40
 */
public interface UserGateway {
    boolean save(User user);
    User getUserByUsername(String username);
}
