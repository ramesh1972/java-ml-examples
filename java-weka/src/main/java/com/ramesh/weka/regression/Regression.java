package com.ramesh.weka.regression;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.SMOreg;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;

/***
 * THis project demonstrates regressing the input data to predict values based on new inputs
 * 3 different training models are used - linear regression, SMO, SVM
 */
public class Regression {

	public static void main(String[] args) {
		try {
			// load the house training dataset
			DataSource source = new DataSource("house-train.arff");
			Instances dataset = source.getDataSet();
			dataset.setClassIndex(dataset.numAttributes() - 1);

			// load the house test data
			DataSource source2 = new DataSource("house-test.arff");
			Instances dataset2 = source2.getDataSet();
			dataset2.setClassIndex(dataset2.numAttributes() - 1);

			// train the test data using linear regression model
			LinearRegression lr = new LinearRegression();
			lr.buildClassifier(dataset);

			// evaluate the above model
			Evaluation lreval = new Evaluation(dataset2);
			lreval.evaluateModel(lr, dataset2);

			System.out.println("---------------------------------------------------------");
			System.out.println("linear regression model evaluation summary" + "\n");
			System.out.println(lreval.toSummaryString());

			// tran the test data using SMO regression model
			SMOreg smoreg = new SMOreg();
			smoreg.buildClassifier(dataset);

			// evaluate the above model
			Evaluation svmregeval = new Evaluation(dataset2);
			svmregeval.evaluateModel(smoreg, dataset2);
			System.out.println(svmregeval.toSummaryString());

			System.out.println("---------------------------------------------------------");
			System.out.println("SMO regression model evaluation summary" + "\n");

			System.out.println("-----------------------------------");
			for (int j = 0; j < dataset2.numInstances(); j++) {
				Instance newInst = dataset2.instance(j);

				// classify instance using liner regression and SMO models
				double price2 = lr.classifyInstance(newInst);
				double price = smoreg.classifyInstance(newInst);

				System.out.println("Price of house instance " + newInst + " found using Linear Regession is " + price2);
				System.out.println("Price of house instance " + newInst + " found using SMO Regression is " + price);
				System.out.println("-----------------------------------");
			}
		} catch (Exception e) {
			System.out.print("Unhandled expception " + e.getMessage());
		}
	}
}
