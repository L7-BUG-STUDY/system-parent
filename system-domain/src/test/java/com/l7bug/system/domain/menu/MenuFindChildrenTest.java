package com.l7bug.system.domain.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Menu类findChildren方法的单元测试
 * 重点测试子节点查询和排序功能
 */
class MenuFindChildrenTest {

	private MenuGateway menuGateway;
	private Menu root;

	@BeforeEach
	void setUp() {
		menuGateway = Mockito.mock(MenuGateway.class);
		root = new Menu(menuGateway);
		root.setId(1L);
		root.setFatherId(Menu.ROOT_ID);
		root.setFullId("/1");
	}

	/**
	 * 测试当没有子节点时的情况
	 */
	@Test
	void findChildren_whenNoChildren_shouldSetEmptyPriorityQueue() {
		// 配置mock，返回空列表
		when(menuGateway.findLikeFullId(root.getFullId())).thenReturn(Collections.emptyList());

		// 执行findChildren方法
		root.findChildren();

		// 验证结果
		assertThat(root.getChildren()).isEmpty();
	}

	/**
	 * 测试子节点按sort字段正确排序的情况
	 */
	@Test
	void findChildren_withMultipleChildren_shouldSortBySortField() {
		// 创建子节点并设置不同的sort值
		Menu child1 = createMenu(2L, root.getId(), root.getFullId(), 3); // sort = 3
		Menu child2 = createMenu(3L, root.getId(), root.getFullId(), 1); // sort = 1
		Menu child3 = createMenu(4L, root.getId(), root.getFullId(), 2); // sort = 2

		List<Menu> childrenList = Arrays.asList(child1, child2, child3);

		// 配置mock
		when(menuGateway.findLikeFullId(root.getFullId())).thenReturn(childrenList);

		// 执行findChildren方法
		root.findChildren();

		// 验证子节点已按sort字段正确排序（1, 2, 3）
		// 注意：PriorityQueue的iterator()不保证顺序，我们需要通过poll()来验证排序
		PriorityQueue<Menu> sortedChildren = new PriorityQueue<>(root.getChildren());
		for (Menu sortedChild : sortedChildren) {
			System.err.println(sortedChild.getSort());
		}
		System.err.println("#############################################");
		var temp = new PriorityQueue<>(sortedChildren);
		while (!temp.isEmpty()) {
			System.err.println(temp.poll().getSort());
		}
		System.err.println("#############################################");
		assertThat(sortedChildren.poll().getSort()).isEqualTo(1);
		assertThat(sortedChildren.poll().getSort()).isEqualTo(2);
		assertThat(sortedChildren.poll().getSort()).isEqualTo(3);
	}

	/**
	 * 测试sort值相同时按id排序的情况
	 */
	@Test
	void findChildren_withSameSortValues_shouldSortById() {
		// 创建子节点并设置相同的sort值但不同的id
		Menu child1 = createMenu(5L, root.getId(), root.getFullId(), 1); // id = 5, sort = 1
		Menu child2 = createMenu(2L, root.getId(), root.getFullId(), 1); // id = 2, sort = 1
		Menu child3 = createMenu(8L, root.getId(), root.getFullId(), 1); // id = 8, sort = 1

		List<Menu> childrenList = Arrays.asList(child1, child2, child3);

		// 配置mock
		when(menuGateway.findLikeFullId(root.getFullId())).thenReturn(childrenList);

		// 执行findChildren方法
		root.findChildren();

		// 验证子节点已按id正确排序
		// 注意：PriorityQueue的iterator()不保证顺序，我们需要通过poll()来验证排序
		PriorityQueue<Menu> sortedChildren = new PriorityQueue<>(root.getChildren());
		assertThat(sortedChildren.poll().getId()).isEqualTo(2L);
		assertThat(sortedChildren.poll().getId()).isEqualTo(5L);
		assertThat(sortedChildren.poll().getId()).isEqualTo(8L);
	}

	/**
	 * 测试多层级子节点的正确构建
	 */
	@Test
	void findChildren_withMultiLevelChildren_shouldBuildTreeCorrectly() {
		// 创建多层级节点结构
		// 根节点
		// |- 子节点1 (id=2)
		// |  |- 孙节点1 (id=4)
		// |  |- 孙节点2 (id=5)
		// |- 子节点2 (id=3)
		//    |- 孙节点3 (id=6)

		Menu child1 = createMenu(2L, root.getId(), root.getFullId(), 1);
		Menu child2 = createMenu(3L, root.getId(), root.getFullId(), 2);

		Menu grandChild1 = createMenu(4L, child1.getId(), child1.getFullId(), 1);
		Menu grandChild2 = createMenu(5L, child1.getId(), child1.getFullId(), 2);
		Menu grandChild3 = createMenu(6L, child2.getId(), child2.getFullId(), 1);

		List<Menu> allNodes = Arrays.asList(child1, child2, grandChild1, grandChild2, grandChild3);

		// 配置mock
		when(menuGateway.findLikeFullId(root.getFullId())).thenReturn(allNodes);

		// 执行findChildren方法
		root.findChildren();

		// 验证根节点的直接子节点数量
		assertThat(root.getChildren()).hasSize(2);

		// 验证根节点的直接子节点排序
		// 注意：PriorityQueue的iterator()不保证顺序，我们需要通过poll()来验证排序
		PriorityQueue<Menu> sortedRootChildren = new PriorityQueue<>(root.getChildren());
		assertThat(sortedRootChildren.poll().getId()).isEqualTo(2L);
		assertThat(sortedRootChildren.poll().getId()).isEqualTo(3L);

		// 验证子节点1的子节点
		Menu actualChild1 = getChildById(root.getChildren(), 2L);
		assertThat(actualChild1).isNotNull();
		assertThat(actualChild1.getChildren()).hasSize(2);

		// 验证子节点1的子节点排序
		// 注意：PriorityQueue的iterator()不保证顺序，我们需要通过poll()来验证排序
		PriorityQueue<Menu> sortedChild1Children = new PriorityQueue<>(actualChild1.getChildren());
		assertThat(sortedChild1Children.poll().getId()).isEqualTo(4L);
		assertThat(sortedChild1Children.poll().getId()).isEqualTo(5L);

		// 验证子节点2的子节点
		Menu actualChild2 = getChildById(root.getChildren(), 3L);
		assertThat(actualChild2).isNotNull();
		assertThat(actualChild2.getChildren()).hasSize(1);

		// 验证子节点2的子节点排序
		// 注意：PriorityQueue的iterator()不保证顺序，我们需要通过poll()来验证排序
		PriorityQueue<Menu> sortedChild2Children = new PriorityQueue<>(actualChild2.getChildren());
		assertThat(sortedChild2Children.poll().getId()).isEqualTo(6L);
	}

	/**
	 * 测试子节点中有null sort值的情况
	 */
	@Test
	void findChildren_withNullSortValues_shouldHandleNullsCorrectly() {
		// 创建子节点，其中一些sort值为null
		Menu child1 = createMenu(2L, root.getId(), root.getFullId(), 2);
		Menu child2 = createMenu(3L, root.getId(), root.getFullId(), null); // null sort
		Menu child3 = createMenu(4L, root.getId(), root.getFullId(), 1);

		List<Menu> childrenList = Arrays.asList(child1, child2, child3);

		// 配置mock
		when(menuGateway.findLikeFullId(root.getFullId())).thenReturn(childrenList);

		// 执行findChildren方法
		root.findChildren();

		// 验证null值的节点排在前面
		// 注意：PriorityQueue的iterator()不保证顺序，我们需要通过poll()来验证排序
		PriorityQueue<Menu> sortedChildren = new PriorityQueue<>(root.getChildren());
		assertThat(sortedChildren.poll().getId()).isEqualTo(3L); // null sort的节点应在最前
		assertThat(sortedChildren.poll().getId()).isEqualTo(4L);
		assertThat(sortedChildren.poll().getId()).isEqualTo(2L);
	}

	/**
	 * 测试复杂排序场景：多种sort值和id组合
	 */
	@Test
	void findChildren_complexScenario_shouldSortCorrectly() {
		// 创建复杂的节点组合
		Menu child1 = createMenu(10L, root.getId(), root.getFullId(), 2);
		Menu child2 = createMenu(1L, root.getId(), root.getFullId(), 1);
		Menu child3 = createMenu(5L, root.getId(), root.getFullId(), null);
		Menu child4 = createMenu(3L, root.getId(), root.getFullId(), 1); // 与child2相同的sort值
		Menu child5 = createMenu(7L, root.getId(), root.getFullId(), 2); // 与child1相同的sort值

		List<Menu> childrenList = Arrays.asList(child1, child2, child3, child4, child5);

		// 配置mock
		when(menuGateway.findLikeFullId(root.getFullId())).thenReturn(childrenList);

		// 执行findChildren方法
		root.findChildren();

		// 验证复杂排序结果：
		// 1. sort为null的节点(5L)排在最前
		// 2. sort为1的节点按id排序：1L, 3L
		// 3. sort为2的节点按id排序：7L, 10L
		// 注意：PriorityQueue的iterator()不保证顺序，我们需要通过poll()来验证排序
		PriorityQueue<Menu> sortedChildren = new PriorityQueue<>(root.getChildren());
		assertThat(sortedChildren.poll().getId()).isEqualTo(5L); // null sort的节点应在最前
		assertThat(sortedChildren.poll().getId()).isEqualTo(1L); // sort=1,id=1的节点
		assertThat(sortedChildren.poll().getId()).isEqualTo(3L); // sort=1,id=3的节点
		assertThat(sortedChildren.poll().getId()).isEqualTo(7L); // sort=2,id=7的节点
		assertThat(sortedChildren.poll().getId()).isEqualTo(10L); // sort=2,id=10的节点
	}

	/**
	 * 辅助方法：根据id从PriorityQueue中获取指定的Menu
	 */
	private Menu getChildById(PriorityQueue<Menu> children, Long id) {
		for (Menu child : children) {
			if (child.getId().equals(id)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * 辅助方法：创建测试用的Menu对象
	 */
	private Menu createMenu(Long id, Long fatherId, String fatherFullId, Integer sort) {
		Menu menu = new Menu(menuGateway);
		menu.setId(id);
		menu.setFatherId(fatherId);
		menu.setFullId(fatherFullId + Menu.PATH_SEPARATOR + id);
		menu.setSort(sort);
		return menu;
	}
}
