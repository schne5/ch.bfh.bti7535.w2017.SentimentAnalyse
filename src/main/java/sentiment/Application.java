package sentiment;

import features.SentimentWordCountResult;
import features.SentimentWordCounter;
import helper.ArffFileGenerator;

import java.io.File;
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
		// test stop words elimination
		Path pathTestFile = Paths.get("goldstandard\\pos", "cv000_29590.txt");
		List<String> origWordList = FileToList.fileToList(pathTestFile);

		List<String> cleandWordList = StopWordElimination.removeStopWords(origWordList);

		System.out.println("Originale Liste: " + origWordList);
		System.out.println("Ohne stop words: " + cleandWordList);

        ArffFileGenerator generator = new ArffFileGenerator(Paths.get("trainingdata"));
        generator.addNumericAttribute("NumOfNegativeWords");
        generator.addNumericAttribute("NumOfPositiveWords");
		generator.addStringAttribute("Sentiment", Arrays.asList("Negativ","Positiv"));
        
        List<String> filesPositive = readAllFiles("goldstandard\\pos");
        List<String> filesNegative = readAllFiles("goldstandard\\neg");
        
        String pathPositive = "goldstandard\\pos";
        String pathNegative = "goldstandard\\neg";
        
        SentimentWordCounter sentimentWordCounter = new SentimentWordCounter();
        
        insertFeature(generator, sentimentWordCounter, pathPositive, filesPositive, true);
        insertFeature(generator, sentimentWordCounter, pathNegative, filesNegative, false);
        
        generator.generateFile();
    }
	
	private static void insertFeature(ArffFileGenerator generator, SentimentWordCounter sentimentWordCounter, String pathPositive, List<String> filesPositive, boolean positive) {
		for (int i = 0; i < 10; i++) {
        	SentimentWordCountResult result = sentimentWordCounter.countSentimentWords(Paths.get(pathPositive, filesPositive.get(i)));
        	generator.addValues(new double[]{result.getNegativeWordCount(), result.getPositiveWordCount(), positive ? 1 : 0});
        }
	}
	
	private static List<String> readAllFiles(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		List<String> fileNames = new ArrayList<String>();

	    for (File file : files) {
	    	if (file.isFile()) {
	            fileNames.add(file.getName());
	          }
	    }
	    
	    return fileNames;
	}
}
