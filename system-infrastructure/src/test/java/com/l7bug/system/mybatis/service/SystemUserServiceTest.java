package com.l7bug.system.mybatis.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SystemUserServiceTest {
	@Autowired
	private UserGateway userGateway;

	@Autowired
	private SystemUserService systemUserService;

	@Test
	void saveRoot() {
		User root = userGateway.getUserByUsername("root");
		if (root == null) {
			root = new User(userGateway);
			root.setUsername("root");
		}
		root.setEnable();
		root.setNickname("root");
		root.setRawPassword("123456");
		root.save();
		User admin = userGateway.getUserByUsername("admin");

		if (admin == null) {
			admin = new User(userGateway);
			admin.setUsername("admin");
		}
		admin.setEnable();
		admin.setNickname(IdUtil.getSnowflakeNextIdStr());
		admin.setRawPassword(admin.getNickname());
		admin.save();
	}

	@Test
	void all() throws JsonProcessingException {
		systemUserService.list(Wrappers.lambdaQuery(SystemUser.class).last("limit 10")).forEach(System.err::println);
	}
}
