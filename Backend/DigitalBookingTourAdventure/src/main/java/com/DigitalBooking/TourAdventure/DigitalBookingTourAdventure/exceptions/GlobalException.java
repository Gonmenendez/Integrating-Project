package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {


    @ExceptionHandler({BadIdRequestException.class})
    public ResponseEntity<String> invalidId(BadIdRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({BadNameRequestException.class})
    public ResponseEntity<String> invalidName(BadNameRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({TourNameAlreadyExistsException.class})
    public ResponseEntity<String> nameAlreadyExists(TourNameAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({CategoryTitleNotFoundException.class})
    public ResponseEntity<String> nameAlreadyExists(CategoryTitleNotFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler({LocationNameAlreadyExistsException.class})
    public ResponseEntity<String> nameAlreadyExists(LocationNameAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
