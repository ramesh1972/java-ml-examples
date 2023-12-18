package com.ramesh.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/***
 * This project demostrates the detection of sentences in paragraphs based on Open-NLP library
 */
public class SentenceDetectionExample {

	public static void main(String[] args) {
		new SentenceDetectionExample().sentenceDetect();
	}

	/**
	 * This method is used to detect sentences in a paragraph/string
	 * 
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public void sentenceDetect() {
		String paragraph = "This is a statement. This is another statement. Now is an abstract word for time, that is always flying.";

		try {
			// refer to model file "en-sent,bin", available at link
			// http://opennlp.sourceforge.net/models-1.5/
			InputStream is = new FileInputStream("en-sent.bin");
			SentenceModel model = new SentenceModel(is);

			// feed the model to SentenceDetectorME class
			SentenceDetectorME sdetector = new SentenceDetectorME(model);

			// detect sentences in the paragraph
			String sentences[] = sdetector.sentDetect(paragraph);

			// print the sentences detected, to console
			for (int i = 0; i < sentences.length; i++) {
				System.out.println(sentences[i]);
			}
			is.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}