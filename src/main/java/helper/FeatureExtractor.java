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
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeatureExtractor {
	private static final Logger LOGGER = Logger.getLogger( FeatureExtractor.class.getName() );
	
	ArffFileGenerator trainingDataGenerator;
    ArffFileGenerator testDataGenerator;
	
	List<String> filesPositive;
	List<String> filesNegative;
	
	SentimentWordCounter sentimentWordCounter;
    Negator negator;
	
	public FeatureExtractor() {
		trainingDataGenerator = createGenerator();
		testDataGenerator = createGenerator();
		
		filesPositive = readAllFiles(Constants.PATH_POSITIVE);
		filesNegative = readAllFiles(Constants.PATH_NEGATIVE);
		
		// features
		sentimentWordCounter = new SentimentWordCounter();
        negator = new Negator();
	}

    public void extractFeatures(){
        insertFeature(true, 0, 500, false);
        insertFeature(false, 0, 500, false);

        insertFeature(true, 500, 600, true);
        insertFeature(false, 500, 600, true);

        trainingDataGenerator.generateFile(Constants.FILE_NAME_TRAINING);
        testDataGenerator.generateFile(Constants.FILE_NAME_TEST);
    }

    private void insertFeature(boolean positive, int rangeFrom, int rangeTo, boolean isTestData) {
        for (int i = rangeFrom; i < rangeTo; i++) {
        	SentimentWordCountResult sentimentResult = null;
        	NegatorResult negatorResult = null;
        	
			try {
				List<String> input;
				
				if (positive) {
					input = Files.readAllLines(Paths.get(Constants.PATH_POSITIVE, filesPositive.get(i)));
				} else {
					input = Files.readAllLines(Paths.get(Constants.PATH_NEGATIVE, filesNegative.get(i)));
				}
				
				sentimentResult = sentimentWordCounter.countSentimentWords(input);
	            negatorResult = negator.executeNegation(input);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.toString());
			}
            
			if (isTestData) {
				testDataGenerator.addValues(new double[]{sentimentResult.getNegativeWordCount(), sentimentResult.getPositiveWordCount(), negatorResult.getNegatedWordWeight(), Instance.missingValue()});	
			} else {
				trainingDataGenerator.addValues(new double[]{sentimentResult.getNegativeWordCount(), sentimentResult.getPositiveWordCount(), negatorResult.getNegatedWordWeight(), positive ? 1 : 0});
			}
        }
    }
    
    private ArffFileGenerator createGenerator() {
        ArffFileGenerator generator = new ArffFileGenerator(Paths.get(Constants.PATH_TRAINING_TEST_FILES));
        generator.addNumericAttribute("NumOfNegativeWords");
        generator.addNumericAttribute("NumOfPositiveWords");
        generator.addNumericAttribute("NumOfNegatedWords");
        generator.addStringAttribute("Sentiment", Arrays.asList("Negativ", "Positiv"));

        return generator;
    }
    
    private List<String> readAllFiles(String path) {
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
