package ru.practicum.shareit.user;

import java.util.List;

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

	private final UserService service;

	@GetMapping
	@ResponseBody
	public ResponseEntity<List<UserDto>> getAll() {
		log.info("Получен запрос к эндпоинту: 'GET_USERS'. ");
		return ResponseEntity.ok(service.getAll());
	}

	@PostMapping
	@ResponseBody
	public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto, String email) {
		log.info("Получен запрос к эндпоинту: 'POST_USERS'. ");
		return ResponseEntity.ok(this.service.create(userDto));
	}

	@PatchMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto) {
		log.info("Получен запрос к эндпоинту: 'PATCH_USERS'.");
		return ResponseEntity.ok(this.service.edit(id, userDto));
	}

	@GetMapping(value = "/{id}")
	@ResponseBody
	public ResponseEntity<UserDto> get(@PathVariable Long id) {
		log.info("Получен запрос к эндпоинту: 'GET_USERS_ID'.");
		return ResponseEntity.ok(this.service.get(id));
	}

	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public void delete(@PathVariable Long id) {
		log.info("Получен запрос к эндпоинту: 'DELETE_USERS_ID'.");
		this.service.delete(id);
	}

}
