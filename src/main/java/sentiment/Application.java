package sentiment;

import helper.ArffFileGenerator;
import helper.NaiveBayesClassifier;

public class Application {
	
	public static void main(String[] args) throws Exception {
		ArffFileGenerator generator = new ArffFileGenerator();
		generator.setUseNegator(true);
		generator.generateFile();
		
		NaiveBayesClassifier classifier = new NaiveBayesClassifier();
		classifier.setup();
		classifier.crossValidate(10);
		
//		FeatureExtractor featureExtractor = new FeatureExtractor();
//		featureExtractor.extractFeatures();
//		
//		NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier();
//		naiveBayesClassifier.train();
//		naiveBayesClassifier.test();
    }
}
