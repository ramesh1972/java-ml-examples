package com.ramesh.weka.classification;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/***
 * This project demonstrates the use of classifiers to classify decimals as small, medium or large numbers
 */
public class ClassificationDecimals {

	public static void main(String[] args) throws Exception {
		// load train data
		DataSource source = new DataSource("decimals-train.arff");
		Instances dataset = source.getDataSet();
		dataset.setClassIndex(dataset.numAttributes() - 1);

		DataSource source2 = new DataSource("decimals-train.arff");
		Instances testdata = source2.getDataSet();
		testdata.setClassIndex(testdata.numAttributes() - 1);

		// classify using naove bayes model
		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(dataset);

		System.out.println("-----------------------------------");
		
		// predict
		for (int j = 0; j < testdata.numInstances(); j++) {
			double actualClass = testdata.instance(j).classValue();
			String actual = testdata.classAttribute().value((int) actualClass);

			Instance newInst = testdata.instance(j);
			double d = newInst.value(newInst.numAttributes() - 2);

			double preNB = nb.classifyInstance(newInst);
			String predString = testdata.classAttribute().value((int) preNB);
			
			System.out.println("size of decimal " + d + " is "  + predString);
			System.out.println("-----------------------------------");
		}
	}
}
