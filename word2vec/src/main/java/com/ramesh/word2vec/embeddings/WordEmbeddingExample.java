package com.ramesh.word2vec.embeddings;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.SorensenDice;

/***
 * this project demonstrates how to create vectors for words and find the similarity between them
 */
public class WordEmbeddingExample {
    public static void main(String[] args) {
        
    	// Create Word2Vec model and load pre-trained embeddings
        Word2VecModel model = Word2VecModel.fromTextFile(new File("AnnotatedSentences.txt"));

        // Get word vectors
        float[] vector1 = model.getWordVector("example");
        float[] vector2 = model.getWordVector("sample");

        // Calculate similarity between vectors using cosine similarity
        double cosineSimilarity = cosineSimilarity(vector1, vector2);
        System.out.println("Cosine Similarity: " + cosineSimilarity);

        // 4 different ways to find the word/token similarity 
        double jaccardSimilarity = jaccardSimilarity(vector1, vector2);
        System.out.println("Jaccard Similarity: " + jaccardSimilarity);

        double levenshteinSimilarity = normalizedLevenshteinSimilarity("example", "sample");
        System.out.println("Normalized Levenshtein Similarity: " + levenshteinSimilarity);

        double nGramSimilarity = nGramSimilarity("example", "sample");
        System.out.println("NGram Similarity: " + nGramSimilarity);

        double sorensenDiceSimilarity = sorensenDiceSimilarity("example", "sample");
        System.out.println("Sorensen-Dice Similarity: " + sorensenDiceSimilarity);
    }

    private static double cosineSimilarity(float[] vector1, float[] vector2) {
        return new Cosine().similarity(vector1, vector2);
    }

    private static double jaccardSimilarity(float[] vector1, float[] vector2) {
        return new Jaccard().similarity(vector1, vector2);
    }

    private static double normalizedLevenshteinSimilarity(String str1, String str2) {
        return 1.0 - new NormalizedLevenshtein().distance(str1, str2);
    }

    private static double nGramSimilarity(String str1, String str2) {
        return new NGram(2).similarity(str1, str2);
    }

    private static double sorensenDiceSimilarity(String str1, String str2) {
        return new SorensenDice().similarity(str1, str2);
    }
}
