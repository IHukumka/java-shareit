package ru.practicum.shareit.user.model;

import org.springframework.context.annotation.Lazy;

import ru.practicum.shareit.user.dto.UserDto;

@Lazy
public interface UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
