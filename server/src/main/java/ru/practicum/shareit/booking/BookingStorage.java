package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.BookingStatus;

public interface BookingStorage extends JpaRepository<Booking, Long> {

	Page<Booking> findByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(
			long userId, LocalDateTime now, LocalDateTime now1,
			Pageable pageable);

	Page<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(long userId,
			LocalDateTime now, Pageable pageable);

	Page<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(long userId,
			LocalDateTime now, Pageable pageable);

	Page<Booking> findByBooker_IdAndStatusOrderByStartDesc(long userId,
			BookingStatus status, Pageable pageable);

	Page<Booking> findByBooker_IdOrderByStartDesc(long userId,
			Pageable pageable);

	Page<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(long itemId,
			long userId, BookingStatus status, LocalDateTime now,
			Pageable pageable);

	Page<Booking> findByItem_IdAndStatusOrderByStartDesc(long itemId,
			BookingStatus status, Pageable pageable);

	Page<Booking> findByItem_IdOrderByStartDesc(long itemId, Pageable pageable);

	Page<Booking> findByItem_IdAndStatusAndStartBeforeOrderByStartDesc(
			long itemId, BookingStatus approved, LocalDateTime now,
			Pageable pageable);

	Page<Booking> findByItem_IdAndStatusAndStartAfterOrderByStartAsc(
			long itemId, BookingStatus approved, LocalDateTime now,
			Pageable pageable);

	Page<Booking> findByItem_UserIdAndEndAfterAndStartBeforeOrderByStartDesc(
			long ownerId, LocalDateTime now, LocalDateTime now1,
			Pageable pageable);

	Page<Booking> findByItem_UserIdAndEndBeforeOrderByStartDesc(long ownerId,
			LocalDateTime now, Pageable pageable);

	Page<Booking> findByItem_UserIdAndStartAfterOrderByStartDesc(long ownerId,
			LocalDateTime now, Pageable pageable);

	Page<Booking> findByItem_UserIdAndStatusOrderByStartDesc(long ownerId,
			BookingStatus waiting, Pageable pageable);

	Page<Booking> findByItem_UserIdOrderByStartDesc(long ownerId,
			Pageable pageable);

	@Query("SELECT b FROM Booking b WHERE status = 'APPROVED' AND start_date > NOW() GROUP BY id, item_id ORDER BY start_date ASC")
	Page<Booking> findAllByStatusAndStartAfterOrderByStartAsc(
			BookingStatus approved, LocalDateTime now, Pageable pageable);

	@Query("SELECT b FROM Booking b WHERE status = 'APPROVED' AND start_date < NOW() GROUP BY id, item_id ORDER BY start_date DESC")
	Page<Booking> findAllByStatusAndStartBeforeOrderByStartDesc(
			BookingStatus approved, LocalDateTime now, Pageable pageable);

}
