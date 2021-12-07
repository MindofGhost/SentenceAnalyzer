package ru.maistudents.backendsentenceanalyzer.dto;

import java.io.Serializable;

public class OutputWordDTO implements Serializable {
    private final String word;

    private final String role;

    public OutputWordDTO(String word, String role) {
        this.word = word;
        this.role = role;
    }

    public String getWord() {
        return word;
    }

    public String getRole() {
        return role;
    }
}
