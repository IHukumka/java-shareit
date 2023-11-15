package ru.practicum.shareit.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

				@Id
				@GeneratedValue(strategy = GenerationType.IDENTITY)
				private Long id;

				@Column(name = "name")
				private String name;

				@Column(name = "email", unique = true)
				private String email;
}
