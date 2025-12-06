package com.l7bug.system.mapstruct;

import com.l7bug.system.domain.menu.Menu;
import com.l7bug.system.dto.response.MenuNodeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MenuConvertor
 *
 * @author l
 * @since 2025/12/6 12:14
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MenuConvertor {
	MenuNodeResponse mapResponse(Menu menu);
}
