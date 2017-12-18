package sentiment;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import features.StopWordElimination;
import helper.ArffFileGenerator;
import helper.Constants;
import helper.FileToList;
import helper.NaiveBayesClassifier;

import weka.core.Instance;
//import required classes
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.LovinsStemmer;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Application {
	
	public static void main(String[] args) throws Exception {
		
		/*
		 * https://www.programcreek.com/java-api-examples/index.php?source_dir=AIC-SentimentAnalysis-master/AIC/src/main/java/at/tuwien/aic/classify/ClassifyTweet.java
		 */
		
		
//		DataSource source = new DataSource("training_test_files/WekaTest.arff");
//	    Instances dataset = source.getDataSet();
//	    //set class index to the last attribute
//	    dataset.setClassIndex(dataset.numAttributes()-1);
//
//	    //the base classifier
//	    NaiveBayes tree = new NaiveBayes();
//
//	    //the filter
//	    StringToWordVector filter = new StringToWordVector();
//	    filter.setInputFormat(dataset);
//	    filter.setIDFTransform(true);
//	    filter.setUseStoplist(true);
//	    LovinsStemmer stemmer = new LovinsStemmer();
//	    filter.setStemmer(stemmer);
//	    filter.setLowerCaseTokens(true);
//
//	    //Create the FilteredClassifier object
//	    FilteredClassifier fc = new FilteredClassifier();
//	    //specify filter
//	    fc.setFilter(filter);
//	    //specify base classifier
//	    fc.setClassifier(tree);
//	    //Build the meta-classifier
//	    fc.buildClassifier(dataset);
//
////	    System.out.println(tree.graph());
//	    System.out.println(tree);
//	    
//	    System.out.println("finished");
		
		ArffFileGenerator.generateFile();
		
//		FeatureExtractor featureExtractor = new FeatureExtractor();
//		featureExtractor.extractFeatures();
//		
//		NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier();
//		naiveBayesClassifier.train();
//		naiveBayesClassifier.test();
    }
}
