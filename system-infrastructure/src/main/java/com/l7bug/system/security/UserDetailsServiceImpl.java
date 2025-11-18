package com.l7bug.system.security;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
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
			throw new BadCredentialsException(ClientErrorCode.LOGIN_ERROR.getMessage());
		}

		UserDetailsImpl userDetails = new UserDetailsImpl();
		BeanUtils.copyProperties(user, userDetails);
		return userDetails;
	}
}
