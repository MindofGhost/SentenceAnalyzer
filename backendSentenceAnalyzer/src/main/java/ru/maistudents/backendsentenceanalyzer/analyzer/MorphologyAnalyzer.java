package ru.maistudents.backendsentenceanalyzer.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.payloads.PayloadEncoder;
import org.apache.lucene.analysis.payloads.PayloadHelper;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.io.InputStream;

public class MorphologyAnalyzer extends Analyzer {
    private final LuceneMorphology luceneMorph;

    public MorphologyAnalyzer(LuceneMorphology luceneMorph) {
        this.luceneMorph = luceneMorph;
    }

    public MorphologyAnalyzer(String pathToMorph, LetterDecoderEncoder letterDecoderEncoder) throws IOException {
        luceneMorph = new LuceneMorphology(pathToMorph, letterDecoderEncoder);
    }

    public MorphologyAnalyzer(InputStream inputStream, LetterDecoderEncoder letterDecoderEncoder) throws IOException {
        luceneMorph = new LuceneMorphology(inputStream, letterDecoderEncoder);
    }


    @Override
    protected TokenStreamComponents createComponents(String s) {

        StandardTokenizer src = new StandardTokenizer();
        final PayloadEncoder encoder = new PayloadEncoder() {
            @Override
            public BytesRef encode(char[] buffer) {
                final Float payload = Float.valueOf(new String(buffer));
                System.out.println(payload);
                final byte[] bytes = PayloadHelper.encodeFloat(payload);
                return new BytesRef(bytes, 0, bytes.length);
            }

            @Override
            public BytesRef encode(char[] buffer, int offset, int length) {

                final Float payload = Float.valueOf(new String(buffer, offset, length));
                System.out.println(payload);
                final byte[] bytes = PayloadHelper.encodeFloat(payload);

                return new BytesRef(bytes, 0, bytes.length);
            }
        };

        TokenFilter filter = new LowerCaseFilter(src);
        filter = new MorphologyFilter(filter, luceneMorph);

        return new TokenStreamComponents(src::setReader, filter);
    }
}
