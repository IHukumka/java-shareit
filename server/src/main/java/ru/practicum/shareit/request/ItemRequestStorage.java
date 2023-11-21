package ru.practicum.shareit.request;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Repository
public interface ItemRequestStorage extends PagingAndSortingRepository<ItemRequest, Long> {
    List<ItemRequest> findByUser_Id(long userId);

    Page<ItemRequest> findByUserIsNot(User user, Pageable pageable);
}