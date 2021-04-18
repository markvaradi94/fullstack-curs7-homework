package ro.fasttrackit.restaurantsapp.exception;

import lombok.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestaurantControllerAdvice {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    ApiError handleValidationException(ValidationException exception) {
        return new ApiError(exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ApiError handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ApiError(exception.getMessage());
    }
}

@Value
class ApiError {
    String message;
}
