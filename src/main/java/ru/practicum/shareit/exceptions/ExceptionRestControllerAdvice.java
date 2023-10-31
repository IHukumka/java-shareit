package ru.practicum.shareit.exceptions;

import java.io.IOException;
import java.time.Instant;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionRestControllerAdvice {

	@ExceptionHandler({ ConstraintViolationException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleInvalidParameterException(RuntimeException e) {
		sendResponse(HttpStatus.BAD_REQUEST, e);
	}

	@ExceptionHandler({ NotFoundException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleNotFoundException(RuntimeException e) throws IOException {
		sendResponse(HttpStatus.NOT_FOUND, e);
	}

	@ExceptionHandler({ ValidationException.class })
	public void handlerBadValidation(RuntimeException e) throws IOException {
		sendResponse(HttpStatus.CONFLICT, e);
	}

	private ExceptionResponseMessage sendResponse(HttpStatus status, RuntimeException ex) {

		return new ExceptionResponseMessage(Instant.now(), status.value(), status.getReasonPhrase(),
				ex.getClass().toString(), ex.getMessage());
	}
}
