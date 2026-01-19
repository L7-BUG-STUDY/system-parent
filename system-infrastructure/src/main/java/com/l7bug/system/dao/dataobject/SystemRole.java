package com.l7bug.system.dao.dataobject;

import com.l7bug.database.dataobject.BaseNotDeleDo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * SystemRole
 *
 * @author Administrator
 * @since 2026/1/19 11:33
 */
@Getter
@Setter
@Entity
@Table(name = "system_role")
public class SystemRole extends BaseNotDeleDo {
	@Serial
	private static final long serialVersionUID = 501611771464247879L;
	private String code;

	private String fatherCode;

	private String status;

	private String name;

	private String fullCode;

	private Integer sort;

	private String remark;


}
