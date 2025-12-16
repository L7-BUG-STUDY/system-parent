package com.l7bug.system.domain.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Menu类compareTo方法的单元测试
 */
class MenuCompareToTest {

	private MenuGateway menuGateway;
	private Menu menu1;
	private Menu menu2;

	@BeforeEach
	void setUp() {
		menuGateway = Mockito.mock(MenuGateway.class);
		menu1 = new Menu(menuGateway);
		menu2 = new Menu(menuGateway);
	}

	/**
	 * 测试两个具有不同sort值的菜单比较
	 */
	@Test
	void compareTo_withDifferentSortValues() {
		menu1.setSort(1);
		menu2.setSort(2);

		assertThat(menu1.compareTo(menu2)).isLessThan(0);
		assertThat(menu2.compareTo(menu1)).isGreaterThan(0);
	}

	/**
	 * 测试两个具有相同sort值但不同id的菜单比较
	 */
	@Test
	void compareTo_withSameSortValuesButDifferentIds() {
		int sameSortValue = 5;
		menu1.setSort(sameSortValue);
		menu2.setSort(sameSortValue);

		menu1.setId(1L);
		menu2.setId(2L);

		assertThat(menu1.compareTo(menu2)).isLessThan(0);
		assertThat(menu2.compareTo(menu1)).isGreaterThan(0);
	}

	/**
	 * 测试两个具有相同sort值和相同id的菜单比较
	 */
	@Test
	void compareTo_withSameSortValuesAndSameIds() {
		int sameSortValue = 5;
		long sameId = 1L;

		menu1.setSort(sameSortValue);
		menu2.setSort(sameSortValue);

		menu1.setId(sameId);
		menu2.setId(sameId);

		assertThat(menu1.compareTo(menu2)).isZero();
		assertThat(menu2.compareTo(menu1)).isZero();
	}

	/**
	 * 测试第一个菜单sort为null的情况
	 */
	@Test
	void compareTo_firstMenuSortIsNull() {
		menu1.setSort(null);
		menu2.setSort(5);

		assertThat(menu1.compareTo(menu2)).isLessThan(0);
	}

	/**
	 * 测试第二个菜单sort为null的情况
	 */
	@Test
	void compareTo_secondMenuSortIsNull() {
		menu1.setSort(5);
		menu2.setSort(null);

		assertThat(menu2.compareTo(menu1)).isLessThan(0);
	}

	/**
	 * 测试两个菜单sort都为null的情况
	 */
	@Test
	void compareTo_bothMenusSortAreNull() {
		menu1.setSort(null);
		menu2.setSort(null);

		menu1.setId(1L);
		menu2.setId(2L);

		assertThat(menu1.compareTo(menu2)).isLessThan(0);
		assertThat(menu2.compareTo(menu1)).isGreaterThan(0);
	}

	/**
	 * 测试两个菜单sort和id都为null的情况
	 */
	@Test
	void compareTo_bothMenusSortAndIdAreNull() {
		menu1.setSort(null);
		menu2.setSort(null);

		menu1.setId(null);
		menu2.setId(null);

		assertThat(menu1.compareTo(menu2)).isZero();
		assertThat(menu2.compareTo(menu1)).isZero();
	}
}
