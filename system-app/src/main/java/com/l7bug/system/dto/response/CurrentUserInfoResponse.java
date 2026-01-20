package com.l7bug.system.dto.response;

import java.util.Collection;

/**
 * UserInfo
 *
 * @author Administrator
 * @since 2025/11/7 14:22
 */
public record CurrentUserInfoResponse(Long id, String username, String nickname, Collection<String> authorities) {
}
