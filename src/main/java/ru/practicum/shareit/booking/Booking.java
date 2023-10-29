package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Entity
@Data
@Builder
@Table(name = "bookings")
@RequiredArgsConstructor
@AllArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "start_date")
	private LocalDateTime start;

	@Column(name = "end_date")
	private LocalDateTime end;

	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	public enum BookingStatus {
		WAITING("WAITING"), APPROVED("APPROVED"), REJECTED("REJECTED"), CANCELED("CANCELED");

		private String name;

		BookingStatus(String name) {
			this.name = name;
		}

		@JsonValue
		public String getName() {
			return name;
		}

		@JsonCreator
		public static BookingStatus fromName(String name) {

			if (name == null) {
				return null;
			}

			switch (name) {
			case "WAITING": {
				return WAITING;
			}

			case "APPROVED": {
				return APPROVED;
			}

			case "REJECTED": {
				return REJECTED;
			}

			case "CANCELED": {
				return CANCELED;
			}

			default: {
				throw new UnsupportedOperationException(String.format("Неизвестный статус: '%s'", name));
			}
			}
		}
	}
}
