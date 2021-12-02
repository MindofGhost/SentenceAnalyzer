package ru.maistudents.backendsentenceanalyzer.analyzer;

import java.io.IOException;

public class RussianAnalyzer extends MorphologyAnalyzer {
    public RussianAnalyzer() throws IOException {
        super(new RussianLuceneMorphology());
    }
}
