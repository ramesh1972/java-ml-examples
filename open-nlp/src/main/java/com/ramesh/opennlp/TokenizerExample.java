package com.ramesh.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
 
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
 
/**
 * Tokenizer Example in Apache openNLP using TokenizerME class loaded with pre-trained token model
 */
public class TokenizerExample {
 
	public static void main(String[] args) {
		InputStream modelIn = null;
 
		try {
			// create the tokenizer model
			modelIn = new FileInputStream("en-token.bin");
			TokenizerModel model = new TokenizerModel(modelIn);
			TokenizerME tokenizer = new TokenizerME(model);
			
			// tokenize a sentence/phrase
			String tokens[] = tokenizer.tokenize("John is 26 years old.");
			double tokenProbs[] = tokenizer.getTokenProbabilities();
			
			// print the token along with probability
			System.out.println("Token\t: Probability\n-------------------------------");
			for(int i=0;i<tokens.length;i++){
				System.out.println(tokens[i]+"\t: "+tokenProbs[i]);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
	}
}