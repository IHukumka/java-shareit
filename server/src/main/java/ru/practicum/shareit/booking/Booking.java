package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public enum BookingState {
        ALL("ALL"), CURRENT("CURRENT"), FUTURE("FUTURE"), PAST("PAST"), REJECTED("REJECTED"), WAITING("WAITING");

        @JsonCreator
        public static BookingState fromName(String name) {

            if (name == null) {
                return null;
            }

            switch (name) {
            case "WAITING": {
                return WAITING;
            }

            case "ALL": {
                return ALL;
            }

            case "CURRENT": {
                return CURRENT;
            }

            case "FUTURE": {
                return FUTURE;
            }

            case "PAST": {
                return PAST;
            }

            case "REJECTED": {
                return REJECTED;
            }
            default: {
                throw new UnsupportedOperationException(String.format("Неизвестное состояние: '%s'", name));
            }
            }
        }

        private String name;

        BookingState(String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }
    }

    public enum BookingStatus {
        WAITING("WAITING"), APPROVED("APPROVED"), REJECTED("REJECTED"), CANCELED("CANCELED");

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

        private String name;

        BookingStatus(String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }
    }
}
