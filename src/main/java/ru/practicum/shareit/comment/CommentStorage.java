package ru.practicum.shareit.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentStorage extends JpaRepository<Comment, Long> {
    List<Comment> findByItem_Id(long itemId);
}