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

import features.StopWordElimination;
import helper.FileToList;

public class Application {
	private final static String PATH_GOLDSTANDARD = "goldstandard";
	private final static String PATH_POSITIVE = PATH_GOLDSTANDARD + "\\pos";
	private final static String PATH_NEGATIVE = PATH_GOLDSTANDARD + "\\neg";
	private final static String PATH_TRAINING_TEST_FILES = "training_test_files";
	
	public static void main(String[] args) {
		// test stop words elimination
		Path pathTestFile = Paths.get("goldstandard\\pos", "cv000_29590.txt");
		List<String> origWordList = FileToList.fileToList(pathTestFile);

		List<String> cleandWordList = StopWordElimination.removeStopWords(origWordList);

		System.out.println("Originale Liste: " + origWordList);
		System.out.println("Ohne stop words: " + cleandWordList);

		SentimentWordCounter sentimentWordCounter = new SentimentWordCounter();
        ArffFileGenerator trainingDataGenerator = createGenerator();
        ArffFileGenerator testDataGenerator = createGenerator();
        
        List<String> filesPositive = readAllFiles(PATH_POSITIVE);
        List<String> filesNegative = readAllFiles(PATH_NEGATIVE);
        
        insertFeature(trainingDataGenerator, sentimentWordCounter, filesPositive, true, 0, 100, false);
        insertFeature(trainingDataGenerator, sentimentWordCounter, filesNegative, false, 0, 100, false);
        
        insertFeature(testDataGenerator, sentimentWordCounter, filesPositive, true, 100, 110, true);
        insertFeature(testDataGenerator, sentimentWordCounter, filesNegative, false, 100, 110, true);
        
        trainingDataGenerator.generateFile("trainingData.arff");
        testDataGenerator.generateFile("testData.arff");
    }
	
	private static ArffFileGenerator createGenerator() {
		ArffFileGenerator generator = new ArffFileGenerator(Paths.get(PATH_TRAINING_TEST_FILES));
        generator.addNumericAttribute("NumOfNegativeWords");
        generator.addNumericAttribute("NumOfPositiveWords");
		generator.addStringAttribute("Sentiment", Arrays.asList("Negativ", "Positiv"));
		
		return generator;
	}
	
	/*
	 * TODO: add question mark instead of Positiv/Negativ if isTestData is true
	 * Maybe we should use the ArffSaver class rather than generating .arff files by ourselfes? see https://weka.wikispaces.com/Save+Instances+to+an+ARFF+File 
	 */
	private static void insertFeature(ArffFileGenerator generator, SentimentWordCounter sentimentWordCounter, List<String> files, boolean positive, int rangeFrom, int rangeTo, boolean isTestData) {
		for (int i = rangeFrom; i < rangeTo; i++) {
        	SentimentWordCountResult result = sentimentWordCounter.countSentimentWords(Paths.get(positive ? PATH_POSITIVE : PATH_NEGATIVE, files.get(i)));
        	generator.addValues(new double[]{result.getNegativeWordCount(), result.getPositiveWordCount(), positive ? 1 : 0});
//        	generator.addValues(new double[]{result.getNegativeWordCount(), result.getPositiveWordCount(), isTestData ? "?" : positive ? 1 : 0});
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
