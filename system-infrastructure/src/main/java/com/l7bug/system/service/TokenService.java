package com.l7bug.system.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * TokenService
 *
 * @author Administrator
 * @since 2025/11/10 15:55
 */
@Service
@AllArgsConstructor
public class TokenService {

	private final StringRedisTemplate stringRedisTemplate;

	public boolean tokenValid(String token) {
		return stringRedisTemplate.hasKey(buildRedisKey(token));
	}

	public void expireToken(String token) {
		stringRedisTemplate.expire(buildRedisKey(token), 3, TimeUnit.HOURS);
	}

	private String buildRedisKey(String token) {
		return "system:user:token:" + token;
	}
}
