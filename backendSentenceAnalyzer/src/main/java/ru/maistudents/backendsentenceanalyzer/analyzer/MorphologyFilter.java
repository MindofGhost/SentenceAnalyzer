package ru.maistudents.backendsentenceanalyzer.analyzer;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class MorphologyFilter extends TokenFilter {
    private final LuceneMorphology luceneMorph;
    private Iterator<String> iterator;
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);
    private final PositionIncrementAttribute position = addAttribute(PositionIncrementAttribute.class);
    private State state = null;

    public MorphologyFilter(TokenStream tokenStream, LuceneMorphology luceneMorph) {
        super(tokenStream);
        this.luceneMorph = luceneMorph;
    }


    final public boolean incrementToken() throws IOException {
        if (iterator != null) {
            if (iterator.hasNext()) {
                restoreState(state);
                position.setPositionIncrement(0);
                termAtt.setEmpty().append(iterator.next());
                return true;
            } else {
                state = null;
                iterator = null;
            }
        }
        while (true) {
            boolean b = input.incrementToken();
            if (!b) {
                return false;
            }
            if (!keywordAttr.isKeyword() && termAtt.length() > 0) {
                String s = new String(termAtt.buffer(), 0, termAtt.length());
                if (luceneMorph.checkString(s)) {
                    List<String> forms = luceneMorph.getNormalForms(s);
                    if (forms.isEmpty()) {
                        continue;
                    } else if (forms.size() == 1) {
                        termAtt.setEmpty().append(forms.get(0));
                    } else {
                        state = captureState();
                        iterator = forms.iterator();
                        termAtt.setEmpty().append(iterator.next());
                    }
                }
            }
            return true;
        }
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        state = null;
        iterator = null;
    }
}