package com.ramesh.huggingface.nlp;

import java.io.IOException;
import java.util.List;

import ai.djl.ModelException;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.translate.TranslateException;

/***
 * This project demonstrates how to use huggingface java libraries with a simple example of tokenization
 */
public class Tokenizer {

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String text = "DJL is the best.";

        HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance("sentence-transformers/msmarco-distilbert-dot-v5");
    
        List<String> tokens = tokenizer.tokenize(text);
        
        System.out.println(tokens);
        
    }
}