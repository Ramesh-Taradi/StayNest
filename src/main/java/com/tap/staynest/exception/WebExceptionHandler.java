package com.tap.staynest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler({PGNotFoundException.class, RoomNotFoundException.class,
            BookingNotFoundException.class, ReviewNotFoundException.class})
    public ModelAndView handleNotFound(Exception exception) {
        return buildErrorView(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomUnavailableException.class)
    public ModelAndView handleUnavailable(Exception exception) {
        return buildErrorView(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationError(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList()
                .toString();

        return buildErrorView(new RuntimeException(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception exception) {
        return buildErrorView(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ModelAndView buildErrorView(Exception exception, HttpStatus status) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.setStatus(status);
        modelAndView.addObject("status", status.value());
        modelAndView.addObject("message",
                exception.getMessage() != null ? exception.getMessage() : "Something went wrong.");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }
}
