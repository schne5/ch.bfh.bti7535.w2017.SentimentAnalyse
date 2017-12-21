package sentiment;

import helper.ArffFileGenerator;
import helper.NaiveBayesClassifier;

public class Application {
	
	public static void main(String[] args) throws Exception {
		ArffFileGenerator generator = new ArffFileGenerator();
		generator.setUseNegator(true);
		generator.setUseRating(false);
		generator.generateFile();
		
		NaiveBayesClassifier classifier = new NaiveBayesClassifier();
		classifier.setup();
		classifier.crossValidate(10);
    }
}
