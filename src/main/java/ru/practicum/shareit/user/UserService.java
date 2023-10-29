package ru.practicum.shareit.user;

import java.util.List;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

	List<UserDto> getAll();

	UserDto get(Long id);

	UserDto edit(Long id, UserDto user);

	void clearAll();

	void delete(Long id);

	UserDto create(UserDto userDto);

	void checkUser(Long id);

}
