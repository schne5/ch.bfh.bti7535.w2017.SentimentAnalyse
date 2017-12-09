package sentiment;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import features.StopWordElimination;
import helper.FeatureExtractor;
import helper.FileToList;
import helper.NaiveBayesClassifier;

public class Application {
	
	public static void main(String[] args) {
		// test stop words elimination
		Path pathTestFile = Paths.get("goldstandard\\pos", "cv000_29590.txt");
		List<String> origWordList = FileToList.fileToList(pathTestFile);

		List<String> cleandWordList = StopWordElimination.removeStopWords(origWordList);

		System.out.println("Originale Liste: " + origWordList);
		System.out.println("Ohne stop words: " + cleandWordList);

		FeatureExtractor.extractFeatures();
		NaiveBayesClassifier naiveBayesClassifier = new NaiveBayesClassifier();
		naiveBayesClassifier.train();
		naiveBayesClassifier.test();
    }
}
