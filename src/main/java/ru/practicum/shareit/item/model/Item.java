package ru.practicum.shareit.item.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Entity
@Data
@Builder
@Table(name = "items")
@RequiredArgsConstructor
@AllArgsConstructor
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@NotBlank
	@Size(max = 255)
	@Column(name = "name")
	private String name;

	@NotNull
	@Size(max = 512)
	@Column(name = "description")
	private String description;

	@Column(name = "available")
	private Boolean available;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "request_id", referencedColumnName = "id")
	private ItemRequest request;
}
