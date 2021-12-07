package ru.maistudents.backendsentenceanalyzer.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LuceneMorphology extends MorphologyImpl {

    public LuceneMorphology(String fileName, LetterDecoderEncoder decoderEncoder) throws IOException {
        super(fileName, decoderEncoder);
    }

    public LuceneMorphology(InputStream inputStream, LetterDecoderEncoder decoderEncoder) throws IOException {
        super(inputStream, decoderEncoder);
    }

    protected void readRules(BufferedReader bufferedReader) throws IOException {
        String s;
        int amount;
        s = bufferedReader.readLine();
        amount = Integer.parseInt(s);
        rules = new Heuristic[amount][];
        for (int i = 0; i < amount; i++) {
            String s1 = bufferedReader.readLine();
            int ruleLength = Integer.parseInt(s1);
            Heuristic[] heuristics = new Heuristic[ruleLength];
            for (int j = 0; j < ruleLength; j++) {
                heuristics[j] = new Heuristic(bufferedReader.readLine());
            }
            rules[i] = modifyHeuristic(heuristics);
        }
    }


    private Heuristic[] modifyHeuristic(Heuristic[] heuristics) {
        ArrayList<Heuristic> result = new ArrayList<Heuristic>();
        for (Heuristic heuristic : heuristics) {
            boolean isAdded = true;
            for (Heuristic ch : result) {
                isAdded = isAdded && !(ch.getActualNormalSuffix().equals(heuristic.getActualNormalSuffix()) && (ch.getActualSuffixLength() == heuristic.getActualSuffixLength()));
            }
            if (isAdded) {
                result.add(heuristic);
            }
        }
        return result.toArray(new Heuristic[result.size()]);
    }

    public boolean checkString(String s) {
        return decoderEncoder.checkString(s);
    }
}