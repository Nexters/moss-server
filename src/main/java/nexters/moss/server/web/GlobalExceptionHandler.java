package nexters.moss.server.web;

import nexters.moss.server.config.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HabikeryTokenAuthenticatedException.class)
    public ResponseEntity handleHabikeryTokenAuthenticatedException(HabikeryTokenAuthenticatedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HabikeryTokenExpiredException.class)
    public ResponseEntity handleHabikeryTokenExpiredException(HabikeryTokenExpiredException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HabikeryUserDuplicatedException.class)
    public ResponseEntity handleHabikeryUserDuplicatedException(HabikeryUserDuplicatedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HabikeryUserNotFoundException.class)
    public ResponseEntity handleHabikeryUserNotFoundException(HabikeryUserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SocialTokenExpiredException.class)
    public ResponseEntity handleSocialTokenExpiredException(SocialTokenExpiredException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SocialUserNotFoundException.class)
    public ResponseEntity handleSocialUserNotFoundException(SocialUserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyDoneHabitException.class)
    public ResponseEntity handleAlreadyDoneHabitException(AlreadyDoneHabitException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
