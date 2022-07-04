package com.seeds.uc.dto.mapstruct;

import com.seeds.uc.dto.InterUserInfo;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    @Mapping(source = "id", target = "uid")
    UserDto userToDto(User user);

    InterUserInfo userToInterUserInfo(User user);
}
