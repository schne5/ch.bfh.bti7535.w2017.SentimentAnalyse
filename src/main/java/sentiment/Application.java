package sentiment;

import features.SentimentWordCountResult;
import features.SentimentWordCounter;
import helper.ArffFileGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import features.SentimentWordCountResult;
import features.SentimentWordCounter;
import features.StopWordElimination;
import helper.FileToList;

public class Application {
	public static void main(String[] args) {

		// test SentimentCounter
		Path path = Paths.get("goldstandard\\pos", "cv000_29590.txt");
		SentimentWordCountResult result = new SentimentWordCounter().countSentimentWords(path);
		System.out.println("Negative: " + result.getNegativeWordCount());
		System.out.println("Positive: " + result.getPositiveWordCount());

		// test stop words elimination
		Path pathTestFile = Paths.get("goldstandard\\pos", "cv000_29590.txt");
		List<String> origWordList = FileToList.fileToList(pathTestFile);

		List<String> cleandWordList = StopWordElimination.removeStopWords(origWordList);

		System.out.println("Originale Liste: " + origWordList);
		System.out.println("Ohne stop words: " + cleandWordList);

        ArffFileGenerator generator = new ArffFileGenerator(path);
        generator.addNumericAttribute("NumOfNegativeWords");
        generator.addNumericAttribute("NumOfPositiveWords");
		generator.addStringAttribute("Sentiment", Arrays.asList("Negativ","Positiv"));
        generator.addValues(new double[]{result.getNegativeWordCount(),result.getPositiveWordCount(),1});
        generator.generateFile();
    }
}
