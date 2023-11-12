package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.shareit.booking.Booking.BookingStatus;

public interface BookingStorage extends JpaRepository<Booking, Long> {

	Page<Booking> findByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(long userId, LocalDateTime now,
			LocalDateTime now1, Pageable pageable);

	Page<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime now, Pageable pageable);

	Page<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime now, Pageable pageable);

	Page<Booking> findByBooker_IdAndStatusOrderByStartDesc(long userId, BookingStatus status, Pageable pageable);

	Page<Booking> findByBooker_IdOrderByStartDesc(long userId, Pageable pageable);

	Page<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(long itemId, long userId, BookingStatus status,
			LocalDateTime now, Pageable pageable);

	Page<Booking> findByItem_IdAndStatusOrderByStartDesc(long itemId, BookingStatus status, Pageable pageable);

	Page<Booking> findByItem_IdOrderByStartDesc(long itemId, Pageable pageable);

	Page<Booking> findByItem_UserIdAndEndAfterAndStartBeforeOrderByStartDesc(long ownerId, LocalDateTime now,
			LocalDateTime now1, Pageable pageable);

	Page<Booking> findByItem_UserIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime now, Pageable pageable);

	Page<Booking> findByItem_UserIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime now, Pageable pageable);

	Page<Booking> findByItem_UserIdAndStatusOrderByStartDesc(long ownerId, BookingStatus waiting, Pageable pageable);

	Page<Booking> findByItem_UserIdOrderByStartDesc(long ownerId, Pageable pageable);

	Booking findFirstByItem_IdAndStatusAndStartAfterOrderByStartAsc(long itemId, BookingStatus approved,
			LocalDateTime now);

	Booking findFirstByItem_IdAndStatusAndStartBeforeOrderByStartDesc(long itemId, BookingStatus approved,
			LocalDateTime now);
}
