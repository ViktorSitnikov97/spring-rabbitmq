package exercise.mapper;

import exercise.dto.UserCreateDTO;
import exercise.dto.UserDTO;
import exercise.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    public abstract User map(UserCreateDTO model);
    public abstract UserDTO map(User model);
}

