package com.l7bug.system.dao.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.l7bug.system.dao.dataobject.SystemMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @description 针对表【system_menu(菜单表)】的数据库操作Mapper
 * @createDate 2025-12-02 18:08:11
 * @Entity com.l7bug.system.mybatis.dataobject.SystemMenu
 */
@Repository
@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {
}




