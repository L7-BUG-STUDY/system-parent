package com.l7bug.system.mybatis.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.l7bug.database.dataobject.BaseNotDeleDo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * UserDo
 *
 * @author Administrator
 * @since 2025/11/7 10:58
 */
@TableName("system_users")
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemUser extends BaseNotDeleDo {
	@Serial
	private static final long serialVersionUID = 8094785838755617085L;
	private String username;
	private String nickname;
	private String password;
	private Integer status;
}
