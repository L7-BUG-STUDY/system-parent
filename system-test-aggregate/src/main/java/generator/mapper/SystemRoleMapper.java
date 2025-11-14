package generator.mapper;

import generator.domain.SystemRole;

/**
* @author Administrator
* @description 针对表【system_role(用户表)】的数据库操作Mapper
* @createDate 2025-11-13 12:15:55
* @Entity generator.domain.SystemRole
*/
public interface SystemRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SystemRole record);

    int insertSelective(SystemRole record);

    SystemRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemRole record);

    int updateByPrimaryKey(SystemRole record);

}
