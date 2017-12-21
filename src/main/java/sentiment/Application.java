package sentiment;

import classifier.BaseLineClassifier;
import helper.ArffFileGenerator;
import classifier.NaiveBayesClassifier;
import helper.Document;
import helper.Util;

import java.util.List;

public class Application {
	
	public static void main(String[] args) throws Exception {
		List<Document> documents = Util.getAllDocuments();
		/*ArffFileGenerator generator = new ArffFileGenerator();
		generator.setRemoveStopWords(false);
		generator.setUseNegator(true);
		generator.setUseRating(false);
		generator.setUseWordWeightIncreasing(true);
		generator.setUseAdjectiveWordWeightIncreasing(true);
		generator.generateFile();
		
		NaiveBayesClassifier classifier = new NaiveBayesClassifier();
		classifier.setup();
		classifier.crossValidate(10);*/

		BaseLineClassifier baseLineClassifier = new BaseLineClassifier();
		baseLineClassifier.crossValidate(documents);
    }
}
