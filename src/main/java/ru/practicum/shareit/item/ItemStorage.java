package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemStorage extends JpaRepository<Item, Long> {

				List<Item> findByRequest_Id(long requestId);

				Page<Item> findByUser_IdOrderById(long userId, Pageable pageable);

				@Query("SELECT i FROM Item i WHERE LOWER(i.description) LIKE %:text% AND i.available = true")
				List<Item> findAllItemsByDescriptionContainingIgnoreCaseAndAvailableTrue(@Param("text") String text);

}
