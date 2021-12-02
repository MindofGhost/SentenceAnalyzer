package ru.maistudents.backendsentenceanalyzer.analyzer;

public interface LetterDecoderEncoder {
    Integer encode(String string);

    int[] encodeToArray(String s);

    String decode(Integer suffixN);

    boolean checkCharacter(char c);

    boolean checkString(String word);
}
