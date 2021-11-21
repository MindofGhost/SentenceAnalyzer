package ru.maistudents.backendsentenceanalyzer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
public class BackendExceptionHandler {

    @ExceptionHandler(InputTextIsEmpty.class)
    public ResponseEntity<ResponseDTO> handleInputTextIsEmpty(InputTextIsEmpty e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileIsEmptyException.class)
    public ResponseEntity<ResponseDTO> handleFileIsEmptyException(FileIsEmptyException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileWrongFormatException.class)
    public ResponseEntity<ResponseDTO> handleFileWrongFormatException(FileWrongFormatException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileHasBadContentException.class)
    public ResponseEntity<ResponseDTO> handleFileHasBadContentException(FileHasBadContentException e) {
        return new ResponseEntity<>(makeResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ResponseDTO makeResponse(String message) {
        String responseMessage = String.format("%s %s", LocalDateTime.now(), message);
        return new ResponseDTO(responseMessage);
    }
}

class ResponseDTO {
    private String message;

    public ResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
