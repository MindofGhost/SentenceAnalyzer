package ru.maistudents.backendsentenceanalyzer.exceptions;

public class FileIsEmptyException extends RuntimeException {
    public FileIsEmptyException(String message) {
        super(message);
    }
}
