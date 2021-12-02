package ru.maistudents.backendsentenceanalyzer.exceptions;

public class WrongCharacterException extends RuntimeException {
    public WrongCharacterException() {
    }

    public WrongCharacterException(String message) {
        super(message);
    }
}
