package ru.maistudents.backendsentenceanalyzer.exceptions;

public class FileWrongFormatException extends RuntimeException {
    public FileWrongFormatException(String message) {
        super(message);
    }
}
