package sentiment;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import features.StopWordElimination;
import helper.Constants;
import helper.FeatureExtractor;
import helper.FileToList;
import helper.NaiveBayesClassifier;

public class Application {
	
	public static void main(String[] args) {
		FeatureExtractor featureExtractor = new FeatureExtractor();
		featureExtractor.extractFeatures();
		
		NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier();
		naiveBayesClassifier.train();
		naiveBayesClassifier.test();
    }
}
