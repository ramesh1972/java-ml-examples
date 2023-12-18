package com.ramesh.weka.classification;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/***
 * This project demonstrates the use of classifiers to classify flowers
 */
public class ClassificationFlowers {
	public static void main(String[] args) throws Exception {
		// load the data for training the model
		DataSource source = new DataSource("iris-train.arff");
		Instances dataset = source.getDataSet();
		dataset.setClassIndex(dataset.numAttributes() - 1);

		// load the data for evaluating the model
		DataSource source2 = new DataSource("iris-test.arff");
		Instances testdata = source2.getDataSet();
		testdata.setClassIndex(testdata.numAttributes() - 1);

		// train the model using naive bayes model
		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(dataset);

		// evalutate the model generated above
		Evaluation eval = new Evaluation(testdata);
		eval.evaluateModel(nb, testdata);

		System.out.println("---------------------------------------------------------");
		
		System.out.println("evaluating the model trainied by Naive Bayes");
		System.out.println(eval.toSummaryString());
		System.out.println("---------------------------------------------------------");

		// train the model using SMO model
		SMO svm = new SMO();
		svm.buildClassifier(dataset);
		Evaluation eval2 = new Evaluation(testdata);

		// evaluate the model generated above
		eval2.evaluateModel(svm, testdata);
		System.out.println("evaluating the model trainied by SMO");
		System.out.println(eval2.toSummaryString());
		System.out.println("---------------------------------------------------------");

		// train the model using SVM model
		LibSVM libsvm = new LibSVM();
		String[] options = new String[8];
		options[0] = "-S";
		options[1] = "0";
		options[2] = "-K";
		options[3] = "2";
		options[4] = "-G";
		options[5] = "1.0";
		options[6] = "-C";
		options[7] = "1.0";
		libsvm.setOptions(options);
		libsvm.buildClassifier(dataset);

		// evaluate the model generated above
		Evaluation eval3 = new Evaluation(testdata);
		eval3.evaluateModel(libsvm, testdata);
		System.out.println("evaluating the model trainied by SVM");
		System.out.println(eval2.toSummaryString());
		System.out.println("---------------------------------------------------------");

		// predictions
		for (int j = 0; j < testdata.numInstances(); j++) {
			try {
				Instance newInst = testdata.instance(j);

				// classify
				double p_nb = nb.classifyInstance(newInst);
				double p_smo = svm.classifyInstance(newInst);
				double p_svm = libsvm.classifyInstance(newInst);

				// get the name of the class value
				String prediction_nb = testdata.classAttribute().value((int) p_nb);
				String prediction_smo = testdata.classAttribute().value((int) p_smo);
				String prediction_svm = testdata.classAttribute().value((int) p_svm);

				System.out.println("The predicted value of instance " + newInst + " using naive bayes " + ": " + prediction_nb);
				System.out.println("The predicted value of instance " + newInst + " using SMO " + ": " + prediction_smo);
				System.out.println("The predicted value of instance " + newInst + " using SVM " + ": " + prediction_svm);

				System.out.println("-----------------------------------");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}
}