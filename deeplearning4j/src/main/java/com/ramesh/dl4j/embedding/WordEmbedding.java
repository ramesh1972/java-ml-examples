package com.ramesh.dl4j.embedding;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;

/***
 * THis project demonstrates how to create vectors for given input text and serialize them to a file
 * Then words in the vectordb (file) are compared and the similarity between them is determined and displayed
 */
public class WordEmbedding {

    public static void main(String[] args) throws Exception {
    	
        // Define your text data file
        File file = new File("text_data.txt");

        // Define a SentenceIterator to read sentences from the file
        SentenceIterator iter = new BasicLineIterator(file);

        // Define TokenizerFactory with CommonPreprocessor
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        System.out.println("Buidling Word2Vec model....");
        
        // Set up Word2Vec model configuration
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(3)
                .iterations(5)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(tokenizerFactory)
                .build();

        // Train the Word2Vec model
        System.out.println("Fitting Word2Vec model....");
        vec.fit();

        // Save the WordVectors to a file
        WordVectorSerializer.writeWordVectors(vec, "vectors.txt");

        // Example of finding similar words
        System.out.println("Words similar to 'rama': " + vec.wordsNearest("rama", 5));

        // Example of calculating the similarity between two words
        double similarity = vec.similarity("rama", "sita");
        System.out.println("Similarity between 'rama' and 'sita': " + similarity);
        
        similarity = vec.similarity("rama", "wife");
        System.out.println("Similarity between 'rama' and 'wife': " + similarity);
    }
}
