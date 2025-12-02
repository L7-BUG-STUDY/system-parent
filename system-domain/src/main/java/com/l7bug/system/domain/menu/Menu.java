package com.l7bug.system.domain.menu;

import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.exception.ClientException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * 菜单
 *
 * @author Administrator
 * @since 2025/12/2 14:32
 */
@Data
public class Menu {
	private static final long ROOT_ID = -1L;
	private static final String PATH_SEPARATOR = "/";
	@Getter(AccessLevel.PRIVATE)
	private final MenuGateway menuGateway;
	/**
	 * id
	 */
	private Long id;

	/**
	 * 父id
	 */
	private Long fatherId;
	/**
	 * 全路径id
	 */
	private String fullId;
	/**
	 * 路径
	 */
	private String path;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 组件路径
	 */
	private String component;
	/**
	 * 菜单类型
	 */
	private MenuType type;
	/**
	 * 元数据
	 */
	private MetaVal meta;

	/**
	 * 排序值
	 */
	private Integer sort;

	/**
	 * 启用状态
	 */
	private Boolean enable;

	/**
	 * 判断是否为根节点
	 *
	 * @return 根节点
	 */
	private boolean isRoot() {
		return this.fatherId == ROOT_ID;
	}

	/**
	 * 新增子节点
	 *
	 * @param menu 子节点
	 */
	public void addChildren(Menu menu) {
		if (menu == null) {
			return;
		}
		menu.setFatherId(this.getId());
		menu.save();
		menu.setFullId(this.getFullId() + PATH_SEPARATOR + menu.getId());
		menu.save();
	}

	/**
	 * 调整父节点
	 *
	 * @param fatherId 父节点id
	 */
	public void moveFather(Long fatherId) {
		Menu fatherMenu = this.getMenuGateway().findById(fatherId);
		// 父节点为空,那自己直接是root节点
		Long saveFatherId = Optional.ofNullable(fatherMenu).map(Menu::getId).orElse(ROOT_ID);
		String saveFullId = Optional.ofNullable(fatherMenu).map(Menu::getFullId).orElse("") + PATH_SEPARATOR + this.getId();
		this.setFatherId(saveFatherId);
		this.setFullId(saveFullId);
		List<Menu> byFullId = this.getMenuGateway().findByFullId(this.getFullId());
		for (Menu menu : byFullId) {
			// 设置子节点
			menu.setFullId(this.getFullId() + PATH_SEPARATOR + menu.getId());
		}
		byFullId.add(this);
		this.getMenuGateway().save(byFullId);
	}

	/**
	 * 保存这条数据
	 */
	public void save() {
		Menu oldData = this.getMenuGateway().findById(this.getId());
		if (oldData == null) {
			this.getMenuGateway().save(this);
			return;
		}
		if (!oldData.getFatherId().equals(this.getFatherId())) {
			// 移动父节点id
			this.moveFather(this.getFatherId());
		} else {
			// 只修改自己
			this.getMenuGateway().save(this);
		}
	}

	/**
	 * 尝试删除这条数据
	 *
	 * @throws com.l7bug.common.exception.ClientException 当子节点不为空时,抛出此异常,并给予提示
	 */
	public void delete() {
		List<Menu> byFullId = this.getMenuGateway().findByFullId(this.getFullId());
		if (!byFullId.isEmpty()) {
			throw new ClientException(ClientErrorCode.CHILDREN_IS_NOT_NULL);
		}
		this.getMenuGateway().deleteById(this.getId());
	}
}
