package com.l7bug.system.security;

import cn.hutool.core.bean.BeanUtil;
import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsServiceImpl
 *
 * @author l
 * @since 2025/11/1 17:37
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserGateway userGateway;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userGateway.getUserByUsername(username);
		if (user == null) {
			throw new ClientException(ClientErrorCode.CLIENT_ERROR);
		}
		if (user.isDisable()) {
			throw new ClientException(ClientErrorCode.CLIENT_ERROR);
		}
		return BeanUtil.copyProperties(user, UserDetailsImpl.class);
	}
}
