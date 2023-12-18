package com.ramesh.dl4j.cnn;

import java.io.File;

import org.datavec.image.loader.CifarLoader;
import org.deeplearning4j.core.storage.StatsStorage;
import org.deeplearning4j.datasets.fetchers.DataSetType;
import org.deeplearning4j.datasets.iterator.impl.Cifar10DataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.BatchNormalization;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.AdaDelta;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * this project demonstrates the use of CNN to create a model based on a dataset
 * In this example we use Cannadian CIFAR data
 * the project creates a model file which can be used to predict various things
 */
//@Slf4j
@SuppressWarnings("FieldCanBeLocal")
public class CIFARClassifier {
	
   protected static final Logger log = LoggerFactory.getLogger(CIFARClassifier.class);

   private static int height = 32;
   private static int width = 32;
   private static int channels = 3;
   private static int numLabels = CifarLoader.NUM_LABELS;
   private static int batchSize = 96;
   private static long seed = 123L;
   private static int epochs = 1;

   public static void main(String[] args) throws Exception {
	   System.out.println("Starting...");
       CIFARClassifier cf = new CIFARClassifier();

       // split the CIFAR dataset for training and evaluation
       Cifar10DataSetIterator cifar = new Cifar10DataSetIterator(batchSize, new int[]{height, width}, DataSetType.TRAIN, null, seed);
       Cifar10DataSetIterator cifarEval = new Cifar10DataSetIterator(batchSize, new int[]{height, width}, DataSetType.TEST, null, seed);

       // train model and eval model
       MultiLayerNetwork model = cf.getModel();
       
       // monitor the progress of training using a UI (web based)
       // pass -Dorg.deeplearning.ui.port=8080 (for e.g) to the java/maven command line
       // then open http://localhost:8080/train/overview to monitor the progress
       System.out.println("Storing in-Memory...");
       UIServer uiServer = UIServer.getInstance();
       StatsStorage statsStorage =  new InMemoryStatsStorage();
       uiServer.attach(statsStorage);
       
       // create listeners to monitor the progress
       model.setListeners(new StatsListener( statsStorage), new ScoreIterationListener(50), new EvaluativeListener(cifarEval, 1, InvocationType.EPOCH_END));

       // train the model
       System.out.println("Fitting...");
       model.fit(cifar, epochs);

       // save the model
       System.out.println("Saving model...");
       model.save(new File(".\\", "cifarmodel.dl4j.zip"), true);

       System.out.println("Exit...");
       System.exit(0);
   }


   public MultiLayerNetwork getModel()  {
	   System.out.println("Building simple convolutional network...");

       MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
           .seed(seed)
           .updater(new AdaDelta())
           .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
           .weightInit(WeightInit.XAVIER)
           .list()
           .layer(new ConvolutionLayer.Builder().kernelSize(3,3).stride(1,1).padding(1,1).activation(Activation.LEAKYRELU)
               .nIn(channels).nOut(32).build())
           .layer(new BatchNormalization())
           .layer(new SubsamplingLayer.Builder().kernelSize(2,2).stride(2,2).poolingType(SubsamplingLayer.PoolingType.MAX).build())

           .layer(new ConvolutionLayer.Builder().kernelSize(1,1).stride(1,1).padding(1,1).activation(Activation.LEAKYRELU)
               .nOut(16).build())
           .layer(new BatchNormalization())
           .layer(new ConvolutionLayer.Builder().kernelSize(3,3).stride(1,1).padding(1,1).activation(Activation.LEAKYRELU)
               .nOut(64).build())
           .layer(new BatchNormalization())
           .layer(new SubsamplingLayer.Builder().kernelSize(2,2).stride(2,2).poolingType(SubsamplingLayer.PoolingType.MAX).build())

           .layer(new ConvolutionLayer.Builder().kernelSize(1,1).stride(1,1).padding(1,1).activation(Activation.LEAKYRELU)
               .nOut(32).build())
           .layer(new BatchNormalization())
           .layer(new ConvolutionLayer.Builder().kernelSize(3,3).stride(1,1).padding(1,1).activation(Activation.LEAKYRELU)
               .nOut(128).build())
           .layer(new BatchNormalization())
           .layer(new ConvolutionLayer.Builder().kernelSize(1,1).stride(1,1).padding(1,1).activation(Activation.LEAKYRELU)
               .nOut(64).build())
           .layer(new BatchNormalization())
           .layer(new ConvolutionLayer.Builder().kernelSize(1,1).stride(1,1).padding(1,1).activation(Activation.LEAKYRELU)
               .nOut(numLabels).build())
           .layer(new BatchNormalization())

           .layer(new SubsamplingLayer.Builder().kernelSize(2,2).stride(2,2).poolingType(SubsamplingLayer.PoolingType.AVG).build())

           .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
               .name("output")
               .nOut(numLabels)
               .dropOut(0.8)
               .activation(Activation.SOFTMAX)
               .build())
           .setInputType(InputType.convolutional(height, width, channels))
           .build();

       System.out.println("Completed Building simple convolutional network...");
       MultiLayerNetwork model = new MultiLayerNetwork(conf);
       model.init();
       return model;
   }

}