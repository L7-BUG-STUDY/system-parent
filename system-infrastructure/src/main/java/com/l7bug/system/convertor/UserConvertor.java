package com.l7bug.system.convertor;

import cn.hutool.core.bean.BeanUtil;
import com.l7bug.system.domain.user.Status;
import com.l7bug.system.domain.user.User;
import com.l7bug.system.mybatis.dataobject.SystemUser;
import org.springframework.stereotype.Component;

/**
 * UserConvertor
 *
 * @author Administrator
 * @since 2025/11/7 12:14
 */
@Component
public class UserConvertor {
    public User mapDomain(SystemUser systemUser) {
        User user = BeanUtil.copyProperties(systemUser, User.class);
        user.setStatus(systemUser.getStatus() == Status.ENABLE.getValue() ? Status.ENABLE : Status.DISABLE);
        return user;
    }

    public SystemUser mapDo(User user) {
        SystemUser systemUser = BeanUtil.copyProperties(user, SystemUser.class);
        systemUser.setStatus(user.getStatus().getValue());
        return systemUser;
    }
}
