package com.tap.staynest.exception;

import com.tap.staynest.dto.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PGNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePGNotFound(PGNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoomNotFound(RoomNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleBookingNotFound(BookingNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleReviewNotFound(ReviewNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(RoomUnavailableException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoomUnavailable(RoomUnavailableException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList()
                .toString();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                status.value(),
                message,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(error);
    }
}
