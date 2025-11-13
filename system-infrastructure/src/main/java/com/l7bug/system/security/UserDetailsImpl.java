package com.l7bug.system.security;

import com.l7bug.system.domain.user.UserStatus;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * UserDetails
 *
 * @author Administrator
 * @since 2025/11/7 15:07
 */
@Data
public class UserDetailsImpl implements UserDetails {
	@Serial
	private static final long serialVersionUID = -6457333811221304131L;
	private Long id;
	private String username;
	private String nickname;
	private String password;
	private UserStatus status;
	private Collection<String> authoritiesSet = List.of("READ", "WRITE");

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Optional.ofNullable(authoritiesSet)
			.orElse(Collections.emptyList())
			.stream()
			.map(SimpleGrantedAuthority::new)
			.toList();
	}

	@Override
	public boolean isEnabled() {
		return status == UserStatus.ENABLE;
	}

}
