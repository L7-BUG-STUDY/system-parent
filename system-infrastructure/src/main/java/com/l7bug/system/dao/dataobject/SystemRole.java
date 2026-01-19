package com.l7bug.system.dao.dataobject;

import com.l7bug.database.dataobject.BaseNotDeleDo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serial;

/**
 * SystemRole
 *
 * @author Administrator
 * @since 2026/1/19 11:33
 */
@Data
@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "system_role")
@EqualsAndHashCode(callSuper = true)
public class SystemRole extends BaseNotDeleDo {
	@Serial
	private static final long serialVersionUID = 501611771464247879L;
	private String code;

	private String fatherFullCode;

	private String status;

	private String name;

	private String fullCode;

	private Integer sort;

	private String remark;


}
