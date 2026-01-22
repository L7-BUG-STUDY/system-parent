package com.l7bug.system.domain.role;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import net.datafaker.Faker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RoleFullCodeTest
 *
 * @author Administrator
 * @since 2026/1/21 11:28
 */
public class RoleFullCodeTest {
	private static final Log log = LogFactory.getLog(RoleFullCodeTest.class);
	private final Faker faker = new Faker(Locale.CHINA);
	private RoleGateway mock;

	private Role root;

	/**
	 * 初始化测试所需的Mock对象，模拟RoleGateway的各种方法行为
	 * <p>
	 * 该方法创建了一个内存Map来存储Role对象，并为RoleGateway接口的以下方法提供了模拟实现：
	 * - save(Collection<Role>): 保存角色集合到内存映射中，并为没有ID的角色分配新的ID
	 * - save(Role): 保存单个角色，将其委托给保存集合的方法
	 * - findById(Long): 根据ID从内存映射中检索角色
	 * - findByFullCode(String): 根据完整路径编码精确匹配角色
	 * - findLikeFullCode(String): 根据完整路径编码前缀匹配多个角色
	 */
	@BeforeEach
	void setUp() {
		// 创建Mock对象和内存存储映射
		mock = new RoleGatewayTestImpl();
		root = new Role(mock);
		root.setName("root");
		root.enabled();
		root.save();
	}

	@DisplayName("移动父节点测试")
	@Test
	void moveFatherTest() {
		//
		// Role node1 = new Role(mock);
		// node1.setName(faker.name().fullName());
		// node1.setCode("1");
		// node1.enabled();
		// node1.setFatherFullCode(Role.PATH_SEPARATOR);
		// node1.save();
		//
		// Role node1_1 = new Role(mock);
		// node1_1.setName(faker.name().fullName());
		// node1_1.setCode("11");
		// node1_1.enabled();
		// node1_1.setFatherFullCode(Role.PATH_SEPARATOR);
		// node1_1.save();
		// node1_1.setFatherFullCode(node1.getFullCode());
		// node1_1.save();
		// System.err.println(root);
		// System.err.println(node1);
		// System.err.println(node1_1);
		//
		//
		// Role node1_temp = new Role(mock);
		// node1_temp.setId(node1.getId());
		// node1_temp.setName(faker.name().fullName());
		// node1_temp.setCode("1");
		// node1_temp.enabled();
		// node1_temp.setFatherFullCode(root.getFullCode());
		// node1_temp.save();
		// log.info(mock.findById(node1_1.getId()));
	}

	@DisplayName("测试未拥有父节点的情况下,自动设置为根节点")
	@Test
	void testSave() {
		Role root = new Role(mock);
		root.setName("root");
		root.enabled();
		root.save();
		assertThat(root).extracting(Role::getFatherId).isEqualTo(Role.ROOT_ID);
		assertThat(root.getFullId()).isNotNull().isEqualTo(Role.PATH_SEPARATOR + root.getId());
		root.save();
		root.save();
		root.save();
		root.save();
		assertThat(root).extracting(Role::getFatherId).isEqualTo(Role.ROOT_ID);
		assertThat(root.getFullId()).isNotNull().isEqualTo(Role.PATH_SEPARATOR + root.getId());
	}

	@DisplayName("测试移动父节点")
	@Test
	void testMoveFather() {
		assertThat(root.getId()).isNotNull();
		Role node1 = new Role(mock);
		node1.setName(faker.name().fullName());
		node1.enabled();
		node1.save();
		assertThat(node1.getId()).isNotNull();
		Role node1_1 = new Role(mock);
		node1_1.setName(faker.name().fullName());
		node1_1.enabled();
		node1_1.setFatherId(node1.getId());
		node1_1.save();
		log.info(node1_1.getFullId());
		Long id = node1.getId();
		node1 = new Role(mock);
		node1.setId(id);
		node1.setName(faker.name().fullName());
		node1.setFatherId(root.getId());
		node1.save();
		assertThat(node1.getFullId()).isEqualTo(root.getFullId() + Role.PATH_SEPARATOR + node1.getId());
		assertThat(node1_1.getFullId()).isEqualTo(root.getFullId() + Role.PATH_SEPARATOR + node1.getId() + Role.PATH_SEPARATOR + node1_1.getId());
	}

	@DisplayName("测试查询子节点")
	@Test
	void findAllChildrenTest() {
		root.findAllChildren();
		assertThat(root.getChildren()).isNotNull().hasSize(0);
	}

	@DisplayName("测试根节点查询子节点逻辑")
	@Test
	void rootFindAllChildrenTest() {
		Role root = new Role(mock);
		root.setFullId("");
		root.findAllChildren();
		root.setId(Role.ROOT_ID);
		root.findAllChildren();
	}

	@DisplayName("测试删除子节点")
	@Test
	void deleteChildrenTest() {
		root.save();
		assertThat(root.getId()).isNotNull();
		Role node1 = new Role(mock);
		node1.setName(faker.name().fullName());
		node1.enabled();
		node1.save();
		node1.setFatherId(root.getId());
		node1.save();
		Assertions.assertThatThrownBy(root::delete)
			.isNotNull()
			.isInstanceOf(ClientException.class)
			.message()
			.contains(ClientErrorCode.CHILDREN_IS_NOT_NULL.getMessage())
		;
	}
}
