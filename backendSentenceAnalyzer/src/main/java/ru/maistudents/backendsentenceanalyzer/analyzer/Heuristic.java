package ru.maistudents.backendsentenceanalyzer.analyzer;

import java.io.Serializable;
import java.util.Objects;

public class Heuristic implements Serializable {
    byte actualSuffixLength;
    String actualNormalSuffix;
    short formMorphInfo;
    short normalFormMorphInfo;

    public Heuristic(String s) {
        String[] strings = s.split("\\|");
        actualSuffixLength = Byte.parseByte(strings[0]);
        actualNormalSuffix = strings[1];
        formMorphInfo = Short.parseShort(strings[2]);
        normalFormMorphInfo = Short.parseShort(strings[3]);
    }

    public Heuristic(byte actualSuffixLength, String actualNormalSuffix, short formMorphInfo, short normalFormMorphInfo) {
        this.actualSuffixLength = actualSuffixLength;
        this.actualNormalSuffix = actualNormalSuffix;
        this.formMorphInfo = formMorphInfo;
        this.normalFormMorphInfo = normalFormMorphInfo;
    }

    public StringBuilder transformWord(String w) {
        if (w.length() - actualSuffixLength < 0) return new StringBuilder(w);
        return new StringBuilder(w.substring(0, w.length() - actualSuffixLength)).append(actualNormalSuffix);
    }

    public byte getActualSuffixLength() {
        return actualSuffixLength;
    }

    public String getActualNormalSuffix() {
        return actualNormalSuffix;
    }

    public short getFormMorphInfo() {
        return formMorphInfo;
    }

    public short getNormalFormMorphInfo() {
        return normalFormMorphInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Heuristic heuristic = (Heuristic) o;

        if (actualSuffixLength != heuristic.actualSuffixLength) return false;
        if (formMorphInfo != heuristic.formMorphInfo) return false;
        if (normalFormMorphInfo != heuristic.normalFormMorphInfo) return false;
        if (!Objects.equals(actualNormalSuffix, heuristic.actualNormalSuffix))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = actualSuffixLength;
        result = 31 * result + (actualNormalSuffix != null ? actualNormalSuffix.hashCode() : 0);
        result = 31 * result + (int) formMorphInfo;
        result = 31 * result + (int) normalFormMorphInfo;
        return result;
    }

    @Override
    public String toString() {
        return "" + actualSuffixLength + "|" + actualNormalSuffix + "|" + formMorphInfo + "|" + normalFormMorphInfo;
    }
}
