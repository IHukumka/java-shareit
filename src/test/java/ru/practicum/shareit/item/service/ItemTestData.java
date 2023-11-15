package ru.practicum.shareit.item.service;

import java.time.LocalDateTime;

import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

public class ItemTestData {
				public static ItemDto itemDto1 = ItemDto.builder().name("item1").description("description1").available(true)
												.build();
				public static ItemDto itemDto2 = ItemDto.builder().name("item2").description("description2").requestId(1L)
												.available(true).build();
				public static ItemDto itemDto4 = ItemDto.builder().id(4L).name("item4").description("description4").available(true)
												.build();
				public static ItemDto itemDtoCreated = ItemDto.builder().name("name").description("descriptionCreated")
												.available(true).build();

				public static CommentDto commentDto = CommentDto.builder().text("comment").authorName("user2")
												.created(LocalDateTime.now()).build();
}