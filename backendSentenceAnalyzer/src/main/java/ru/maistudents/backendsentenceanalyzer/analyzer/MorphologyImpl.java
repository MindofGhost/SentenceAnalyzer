package ru.maistudents.backendsentenceanalyzer.analyzer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MorphologyImpl implements Morphology {
    protected int[][] separators;
    protected short[] rulesId;
    protected Heuristic[][] rules;
    protected String[] grammarInfo;
    protected LetterDecoderEncoder decoderEncoder;


    public MorphologyImpl(String fileName, LetterDecoderEncoder decoderEncoder) throws IOException {
        readFromFile(fileName);
        this.decoderEncoder = decoderEncoder;
    }

    public MorphologyImpl(InputStream inputStream, LetterDecoderEncoder decoderEncoder) throws IOException {
        readFromInputStream(inputStream);
        this.decoderEncoder = decoderEncoder;
    }

    public MorphologyImpl(int[][] separators, short[] rulesId, Heuristic[][] rules, String[] grammarInfo) {
        this.separators = separators;
        this.rulesId = rulesId;
        this.rules = rules;
        this.grammarInfo = grammarInfo;
    }

    public List<String> getNormalForms(String s) {
        ArrayList<String> result = new ArrayList<String>();
        int[] ints = decoderEncoder.encodeToArray(revertWord(s));
        int ruleId = findRuleId(ints);
        boolean notSeenEmptyString = true;
        for (Heuristic h : rules[rulesId[ruleId]]) {
            String e = h.transformWord(s).toString();
            if (e.length() > 0) {
                result.add(e);
            } else if (notSeenEmptyString) {
                result.add(s);
                notSeenEmptyString = false;
            }
        }
        return result;
    }

    public List<String> getMorphInfo(String s) {
        ArrayList<String> result = new ArrayList<String>();
        int[] ints = decoderEncoder.encodeToArray(revertWord(s));
        int ruleId = findRuleId(ints);
        for (Heuristic h : rules[rulesId[ruleId]]) {
            result.add(h.transformWord(s).append("|").append(grammarInfo[h.getFormMorphInfo()]).toString());
        }
        return result;
    }

    protected int findRuleId(int[] ints) {
        int low = 0;
        int high = separators.length - 1;
        int mid = 0;
        while (low <= high) {
            mid = (low + high) >>> 1;
            int[] midVal = separators[mid];

            int comResult = compareToInts(ints, midVal);
            if (comResult > 0)
                low = mid + 1;
            else if (comResult < 0)
                high = mid - 1;
            else
                break;
        }
        if (compareToInts(ints, separators[mid]) >= 0) {
            return mid;
        } else {
            return mid - 1;
        }

    }

    private int compareToInts(int[] i1, int[] i2) {
        int minLength = Math.min(i1.length, i2.length);
        for (int i = 0; i < minLength; i++) {
            int i3 = Integer.compare(i1[i], i2[i]);
            if (i3 != 0) return i3;
        }
        return i1.length - i2.length;
    }

    public void readFromFile(String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileName);
        readFromInputStream(inputStream);
    }

    private void readFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String s = bufferedReader.readLine();
        Integer amount = Integer.valueOf(s);

        readSeparators(bufferedReader, amount);

        readRulesId(bufferedReader, amount);

        readRules(bufferedReader);
        readGrammaInfo(bufferedReader);
        bufferedReader.close();
    }

    private void readGrammaInfo(BufferedReader bufferedReader) throws IOException {
        String s;
        int amount;
        s = bufferedReader.readLine();
        amount = Integer.parseInt(s);
        grammarInfo = new String[amount];
        for (int i = 0; i < amount; i++) {
            grammarInfo[i] = bufferedReader.readLine();
        }
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
            rules[i] = new Heuristic[ruleLength];
            for (int j = 0; j < ruleLength; j++) {
                rules[i][j] = new Heuristic(bufferedReader.readLine());
            }
        }
    }

    private void readRulesId(BufferedReader bufferedReader, Integer amount) throws IOException {
        rulesId = new short[amount];
        for (int i = 0; i < amount; i++) {
            String s1 = bufferedReader.readLine();
            rulesId[i] = Short.parseShort(s1);
        }
    }

    private void readSeparators(BufferedReader bufferedReader, Integer amount) throws IOException {
        separators = new int[amount][];
        for (int i = 0; i < amount; i++) {
            String s1 = bufferedReader.readLine();
            int wordLength = Integer.parseInt(s1);
            separators[i] = new int[wordLength];
            for (int j = 0; j < wordLength; j++) {
                separators[i][j] = Integer.parseInt(bufferedReader.readLine());
            }
        }
    }

    protected String revertWord(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= s.length(); i++) {
            result.append(s.charAt(s.length() - i));
        }
        return result.toString();
    }
}
