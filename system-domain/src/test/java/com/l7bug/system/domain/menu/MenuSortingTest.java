package com.l7bug.system.domain.menu;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.PriorityQueue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 用于验证Menu类排序行为的测试
 */
public class MenuSortingTest {

    @Test
    void testMenuSortingBehavior() {
        MenuGateway menuGateway = Mockito.mock(MenuGateway.class);

        // 创建测试菜单项
        Menu menu1 = new Menu(menuGateway);
        menu1.setId(1L);
        menu1.setSort(2);

        Menu menu2 = new Menu(menuGateway);
        menu2.setId(2L);
        menu2.setSort(null);

        Menu menu3 = new Menu(menuGateway);
        menu3.setId(3L);
        menu3.setSort(1);

        // 测试compareTo方法的直接行为
        assertThat(menu2.compareTo(menu3)).isLessThan(0); // null sort应该小于1
        assertThat(menu3.compareTo(menu1)).isLessThan(0); // 1应该小于2
        assertThat(menu2.compareTo(menu1)).isLessThan(0); // null sort应该小于2

        // 测试PriorityQueue中的排序
        PriorityQueue<Menu> queue = new PriorityQueue<>();
        queue.add(menu1);
        queue.add(menu2);
        queue.add(menu3);

        // 按照排序规则，应该是: menu2(null), menu3(1), menu1(2)
        assertThat(queue.poll().getId()).isEqualTo(2L); // null sort的元素
        assertThat(queue.poll().getId()).isEqualTo(3L); // sort=1的元素
        assertThat(queue.poll().getId()).isEqualTo(1L); // sort=2的元素
    }
}