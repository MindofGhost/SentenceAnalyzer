package ru.maistudents.backendsentenceanalyzer.exceptions;

public class SuffixToLongException extends RuntimeException {
    public SuffixToLongException() {
    }

    public SuffixToLongException(String message) {
        super(message);
    }
}
