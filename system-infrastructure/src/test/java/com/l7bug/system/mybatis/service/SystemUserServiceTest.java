package com.l7bug.system.mybatis.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.l7bug.system.dao.dataobject.SystemUser;
import com.l7bug.system.dao.mybatis.service.SystemUserService;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.domain.user.UserGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 系统用户服务测试类
 * <p>
 * 该测试类用于测试系统用户相关的业务逻辑，包括用户的创建、保存和查询功能。
 * 使用Spring Boot测试环境，集成了MyBatis Plus进行数据库操作。
 *
 * @author system
 * @version 1.0
 */
@SpringBootTest
class SystemUserServiceTest {
	/**
	 * 用户领域网关，用于处理用户相关的业务逻辑操作
	 */
	@Autowired
	private UserGateway userGateway;

	/**
	 * 系统用户服务，提供用户相关的数据库操作方法
	 */
	@Autowired
	private SystemUserService systemUserService;

	/**
	 * 测试保存root和admin用户的方法
	 * <p>
	 * 该方法用于初始化系统管理员用户：
	 * 1. 创建或获取root用户，设置基本信息并保存
	 * 2. 创建或获取admin用户，设置基本信息并保存
	 * <p>
	 * root用户：用户名为root，昵称为root，密码为123456
	 * admin用户：用户名为admin，昵称为雪花ID，密码与昵称相同
	 */
	@Test
	void saveRoot() {
		// 查询或创建root用户
		User root = userGateway.getUserByUsername("root");
		if (root == null) {
			root = new User(userGateway);
			root.setUsername("root");
		}
		// 设置root用户的基本信息
		root.setEnable();
		root.setNickname("root");
		root.setRawPassword("123456");
		root.save();

		// 查询或创建admin用户
		User admin = userGateway.getUserByUsername("admin");

		if (admin == null) {
			admin = new User(userGateway);
			admin.setUsername("admin");
		}
		// 设置admin用户的基本信息
		admin.setEnable();
		admin.setNickname(IdUtil.getSnowflakeNextIdStr());
		admin.setRawPassword(admin.getNickname());
		admin.save();
	}

	/**
	 * 测试查询所有用户的方法
	 * <p>
	 * 该方法用于查询系统中的用户列表，限制返回10条记录，
	 * 并将结果输出到标准错误流中。主要用于调试和验证数据查询功能。
	 *
	 * @throws JsonProcessingException 当JSON处理出现异常时抛出
	 */
	@Test
	void all() throws JsonProcessingException {
		systemUserService.list(Wrappers.lambdaQuery(SystemUser.class).last("limit 10")).forEach(System.err::println);
	}
}
