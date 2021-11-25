package ru.maistudents.backendsentenceanalyzer.exceptions;

public class InputTextIsEmpty extends RuntimeException {
    public InputTextIsEmpty(String message) {
        super(message);
    }
}
