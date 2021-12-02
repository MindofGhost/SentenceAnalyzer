package ru.maistudents.backendsentenceanalyzer.analyzer;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RussianLuceneMorphology extends LuceneMorphology{
    public RussianLuceneMorphology() throws IOException {
        super(RussianLuceneMorphology.class.getClassLoader().getResourceAsStream("morph.info"), new RussianLetterDecoderEncoder());
    }
}
