package ru.maistudents.backendsentenceanalyzer.exceptions;

public class FileHasBadContentException extends RuntimeException {
    public FileHasBadContentException(String message) {
        super(message);
    }
}
