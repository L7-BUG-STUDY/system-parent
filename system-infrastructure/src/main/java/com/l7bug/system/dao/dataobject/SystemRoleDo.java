package com.l7bug.system.dao.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("system_role")
@EqualsAndHashCode(callSuper = true)
public class SystemRoleDo extends BaseNotDeleDo {
	@Serial
	private static final long serialVersionUID = 501611771464247879L;
	private String name;
	private Long fatherId;
	private String fullId;
	private String status;
	private Integer sort;
	private String remark = "";
}
