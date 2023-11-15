package ru.practicum.shareit.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

				private long id;

				@Size(max = 255)
				private String name;

				@Size(max = 255)
				@Email(message = "Email is incorrect")
				private String email;
}
