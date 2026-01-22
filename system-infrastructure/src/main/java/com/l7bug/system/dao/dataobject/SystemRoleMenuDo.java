package com.l7bug.system.dao.dataobject;

import com.l7bug.database.dataobject.BaseNotDeleDo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;

/**
 * SystemRoleMenuDo
 *
 * @author Administrator
 * @since 2026/1/22 17:52
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "system_role_menu")
@EqualsAndHashCode(callSuper = true)
public class SystemRoleMenuDo extends BaseNotDeleDo {
	@Serial
	private static final long serialVersionUID = -1112083121108311536L;

	private Long roleId;

	private Long menuId;
}
