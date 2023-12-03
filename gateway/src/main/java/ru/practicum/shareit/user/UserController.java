package ru.practicum.shareit.user;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

	private final UserClient client;

	@GetMapping
	@ResponseBody
	public ResponseEntity<Object> getAll() {
		log.info("Получен запрос к эндпоинту: 'GET_USERS'. ");
		return client.getAll();
	}

	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> create(@RequestBody @Valid UserDto userDto,
			String email) {
		log.info("Получен запрос к эндпоинту: 'POST_USERS'. ");
		return client.create(userDto);
	}

	@PatchMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Object> update(@PathVariable long id,
			@RequestBody @Valid UserDto userDto) {
		log.info("Получен запрос к эндпоинту: 'PATCH_USERS'.");
		return client.edit(id, userDto);
	}

	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<Object> get(@PathVariable long id) {
		log.info("Получен запрос к эндпоинту: 'GET_USERS_ID'.");
		return client.get(id);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public void delete(@PathVariable long id) {
		log.info("Получен запрос к эндпоинту: 'DELETE_USERS_ID'.");
		client.delete(id);
	}

}
