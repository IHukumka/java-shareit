package ru.practicum.shareit.comment;

public interface CommentMapper {

	public static Comment toComment(CommentDto commentDto) {
		return Comment.builder().id(commentDto.getId()).text(commentDto.getText()).created(commentDto.getCreated())
				.build();
	}

	public static CommentDto toDto(Comment comment) {
		return CommentDto.builder().id(comment.getId()).text(comment.getText())
				.authorName(comment.getAuthor().getName()).created(comment.getCreated()).build();
	}
}