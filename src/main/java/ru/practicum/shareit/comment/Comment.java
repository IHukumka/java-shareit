package ru.practicum.shareit.comment;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

				@Id
				@GeneratedValue(strategy = GenerationType.IDENTITY)
				private long id;

				@Column(name = "text")
				private String text;

				@ManyToOne
				@JoinColumn(name = "item_id", referencedColumnName = "id")
				private Item item;

				@ManyToOne
				@JoinColumn(name = "author_id", referencedColumnName = "id")
				private User author;

				@Column(name = "created")
				private LocalDateTime created;
}