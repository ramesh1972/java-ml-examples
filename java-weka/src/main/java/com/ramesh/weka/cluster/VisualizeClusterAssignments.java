package com.ramesh.weka.cluster;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import weka.clusterers.AbstractClusterer;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.explorer.ClustererAssignmentsPlotInstances;
import weka.gui.visualize.VisualizePanel;

public class VisualizeClusterAssignments {
 

/***
* This project demonstrates the clustering. 
* it clusters the input weather information into sunny, overcast and rainy clusters
*/	
public static void main(String[] args) throws Exception {
    // load data
    Instances train = DataSource.read("weather.arff");

    // some data formats store the class attribute information as well
    if (train.classIndex() != -1)
      throw new IllegalArgumentException("Data cannot have class attribute!");

    // instantiate clusterer
    String[] options = new String[1];
    options[0] = "";
    String classname = "FilteredClusterer";
    
    Clusterer clusterer = AbstractClusterer.forName(classname, options);
    
    // evaluate clusterer
    clusterer.buildClusterer(train);
    ClusterEvaluation eval = new ClusterEvaluation();
    eval.setClusterer(clusterer);
    eval.evaluateClusterer(train);

    // setup visualization
    ClustererAssignmentsPlotInstances plotInstances = new ClustererAssignmentsPlotInstances();
    plotInstances.setClusterer(clusterer);
    plotInstances.setInstances(train);
    plotInstances.setClusterEvaluation(eval);
    plotInstances.setUp();
    
    String name = (new SimpleDateFormat("HH:mm:ss - ")).format(new Date());
    String cname = clusterer.getClass().getName();
    
    if (cname.startsWith("weka.clusterers."))
      name += cname.substring("weka.clusterers.".length());
    else
      name += cname;
    
    name = name + " (" + train.relationName() + ")";
    
    // visualize in weka gui
    VisualizePanel vp = new VisualizePanel();
    vp.setName(name);
    vp.addPlot(plotInstances.getPlotData(cname));

    // display data
    // taken from: ClustererPanel.visualizeClusterAssignments(VisualizePanel)
    JFrame jf = new JFrame("Weka Clusterer Visualize: " + vp.getName());
    jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    jf.setSize(500, 400);
    jf.getContentPane().setLayout(new BorderLayout());
    jf.getContentPane().add(vp, BorderLayout.CENTER);
    jf.setVisible(true);
  }
}
