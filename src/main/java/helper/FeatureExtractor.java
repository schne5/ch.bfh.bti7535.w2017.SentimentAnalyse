package helper;

import features.Negator;
import features.NegatorResult;
import features.SentimentWordCountResult;
import features.SentimentWordCounter;
import weka.core.Instance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeatureExtractor {
    public final static String PATH_TRAINING_TEST_FILES = "training_test_files";
    private final static String PATH_GOLDSTANDARD = "goldstandard";
    private final static String PATH_POSITIVE = PATH_GOLDSTANDARD + "\\pos";
    private final static String PATH_NEGATIVE = PATH_GOLDSTANDARD + "\\neg";
    public final static String FILE_NAME_TRAINING = "trainingData.arff";
    public final static String FILE_NAME_TEST = "testData.arff";

    public static void extractFeatures(){
        SentimentWordCounter sentimentWordCounter = new SentimentWordCounter();
        Negator negator = new Negator();
        
        ArffFileGenerator trainingDataGenerator = createGenerator();
        ArffFileGenerator testDataGenerator = createGenerator();

        List<String> filesPositive = readAllFiles(PATH_POSITIVE);
        List<String> filesNegative = readAllFiles(PATH_NEGATIVE);

        insertFeature(trainingDataGenerator, sentimentWordCounter, negator, filesPositive, true, 0, 500, false);
        insertFeature(trainingDataGenerator, sentimentWordCounter, negator, filesNegative, false, 0, 500, false);

        insertFeature(testDataGenerator, sentimentWordCounter, negator, filesPositive, true, 500, 600, true);
        insertFeature(testDataGenerator, sentimentWordCounter, negator, filesNegative, false, 500, 600, true);

        trainingDataGenerator.generateFile(FILE_NAME_TRAINING);
        testDataGenerator.generateFile(FILE_NAME_TEST);
    }

    private static void insertFeature(ArffFileGenerator generator, SentimentWordCounter sentimentWordCounter, Negator negator, List<String> files, boolean positive, int rangeFrom, int rangeTo, boolean isTestData) {
        for (int i = rangeFrom; i < rangeTo; i++) {
        	SentimentWordCountResult sentimentResult = null;
        	NegatorResult negatorResult = null;
        	
			try {
				List<String> input = Files.readAllLines(Paths.get(positive ? PATH_POSITIVE : PATH_NEGATIVE, files.get(i)));
				sentimentResult = sentimentWordCounter.countSentimentWords(input);
	            negatorResult = negator.executeNegation(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            generator.addValues(new double[]{sentimentResult.getNegativeWordCount(), sentimentResult.getPositiveWordCount(), negatorResult.getNegatedWordWeight(), isTestData ? Instance.missingValue() : positive ? 1 : 0});
        }
    }
    
    private static ArffFileGenerator createGenerator() {
        ArffFileGenerator generator = new ArffFileGenerator(Paths.get(PATH_TRAINING_TEST_FILES));
        generator.addNumericAttribute("NumOfNegativeWords");
        generator.addNumericAttribute("NumOfPositiveWords");
        generator.addNumericAttribute("NumOfNegatedWords");
        generator.addStringAttribute("Sentiment", Arrays.asList("Negativ", "Positiv"));

        return generator;
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
