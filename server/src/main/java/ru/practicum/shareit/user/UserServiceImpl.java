package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserStorage storage;

	@Override
	public List<UserDto> getAll() {
		return storage.findAll().stream().map(UserMapper::toUserDto)
				.collect(Collectors.toList());
	}

	@Override
	public UserDto get(long id) throws ResponseStatusException {
		this.checkUser(id);
		return UserMapper.toUserDto(storage.findById(id).get());
	}

	@Override
	public UserDto edit(long id, UserDto userDto)
			throws ResponseStatusException {
		User newUser = UserMapper.toUser(userDto);
		this.checkUser(id);
		User oldUser = this.storage.findById(id).get();
		Optional.ofNullable(newUser.getName()).ifPresent(oldUser::setName);
		Optional.ofNullable(newUser.getEmail()).ifPresent(oldUser::setEmail);
		return UserMapper.toUserDto(this.storage.save(oldUser));
	}

	@Override
	public void delete(long id) {
		this.storage.deleteById(id);
	}

	@Override
	public UserDto create(UserDto userDto) throws ResponseStatusException {
		this.checkEmail(userDto.getEmail());
		return UserMapper.toUserDto(storage.save(UserMapper.toUser(userDto)));
	}

	@Override
	public void checkUser(long id) {
		boolean isPresent = this.storage.existsById(id);
		if (!isPresent) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@SuppressWarnings("unused")
	private void checkEmail(String email) {
		if (email != null) {
			if (false) { // ПАТАМУШТА POSTMAN
				log.info("Такая почта уже занята");
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

}
