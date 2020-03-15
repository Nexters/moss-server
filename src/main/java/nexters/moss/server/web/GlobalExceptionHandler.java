package nexters.moss.server.web;

import nexters.moss.server.application.dto.ErrorResponse;
import nexters.moss.server.config.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity handleAlreadyExistException(AlreadyExistException e) {
        return new ResponseEntity<>(new ErrorResponse(e), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(new ErrorResponse(e), HttpStatus.UNAUTHORIZED);
    }
}
