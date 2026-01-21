package com.l7bug.system.dao.dataobject;

import com.l7bug.database.dataobject.BaseNotDeleDo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
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
public class SystemRoleDo extends BaseNotDeleDo {
	@Serial
	private static final long serialVersionUID = 501611771464247879L;
	@Size(max = 64)
	@NotNull
	@Column(name = "name", nullable = false, length = 64)
	private String name;
	@NotNull
	@Column(name = "father_id", nullable = false)
	private Long fatherId;
	@Size(max = 1024)
	@NotNull
	@Column(name = "full_id", nullable = false, length = 1024)
	private String fullId;
	@Size(max = 16)
	@NotNull
	@Column(name = "status", nullable = false, length = 16)
	private String status;
	@NotNull
	@ColumnDefault("0")
	@Column(name = "sort", nullable = false)
	private Integer sort;
	@Size(max = 256)
	@NotNull
	@ColumnDefault("''")
	@Column(name = "remark", nullable = false, length = 256)
	private String remark;
}
