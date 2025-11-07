package com.l7bug.system.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

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
	private String password;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("READ"));
	}
}
