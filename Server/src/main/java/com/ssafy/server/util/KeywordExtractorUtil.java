package com.ssafy.server.util;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.*;

import java.util.*;

public class KeywordExtractorUtil {

    public static void main(String[] args) {
        // 입력 텍스트
        String inputText = "목동 재건축은 투자 가치가 높으며, 분당 지역도 재건축이 활발합니다.";

        // CoreNLP 설정
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        props.setProperty("tokenize.language", "ko"); // 한국어 토큰화
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // 문장 분석
        CoreDocument document = new CoreDocument(inputText);
        pipeline.annotate(document);

        // 키워드 추출
        List<String> keywords = extractKeywords(document);
        System.out.println("추출된 키워드: " + keywords);
    }

    public static List<String> extractKeywords(CoreDocument document) {
        List<String> keywords = new ArrayList<>();

        // 각 토큰에 대해 품사 태깅 및 키워드 추출
        for (CoreLabel token : document.tokens()) {
            String word = token.word();
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class); // 품사 태깅

            // 명사(NOUN) 및 고유명사(PROPN)만 키워드로 간주
            if (pos.startsWith("NOUN") || pos.startsWith("PROPN")) {
                keywords.add(word);
            }
        }
        return keywords;
    }
}

