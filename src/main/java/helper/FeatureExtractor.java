package helper;

import features.*;
import weka.core.Instance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeatureExtractor {
	private static final Logger LOGGER = Logger.getLogger( FeatureExtractor.class.getName() );
	
	ArffFileGenerator trainingDataGenerator;
    ArffFileGenerator testDataGenerator;
	
	List<String> filesPositive;
	List<String> filesNegative;
	HashMap<String,Boolean> fileList = new HashMap<String,Boolean>();
	
	SentimentWordCounter sentimentWordCounter;
	TF_IDF tf_idf;
    Negator negator;
	
	public FeatureExtractor() {
		filesPositive = readAllFiles(Constants.PATH_POSITIVE);
		filesNegative = readAllFiles(Constants.PATH_NEGATIVE);
		for (String negFile: filesNegative){
		   fileList.put(negFile,false) ;
        }
        for (String posFile: filesPositive){
            fileList.put(posFile,true) ;
        }
		// features
		sentimentWordCounter = new SentimentWordCounter();


        tf_idf = new TF_IDF(fileList,0.8,4);
        negator = new Negator();

        trainingDataGenerator = createGenerator();
        testDataGenerator = createGenerator();
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
            HashMap<String, Double> vector = null;

			try {
				List<String> input;
				
				if (positive) {
					input = Files.readAllLines(Paths.get(Constants.PATH_POSITIVE, filesPositive.get(i)));
					vector = tf_idf.generateVectorTraining(filesPositive.get(i));
				} else {
					input = Files.readAllLines(Paths.get(Constants.PATH_NEGATIVE, filesNegative.get(i)));
                    vector = tf_idf.generateVectorTraining(filesNegative.get(i));
				}
				
				sentimentResult = sentimentWordCounter.countSentimentWords(input);
	            negatorResult = negator.executeNegation(input);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.toString());
			}

			int vocabularyCount = tf_idf.getVocabularyList().keySet().size() + 3;
			double[] values = new double[vocabularyCount];
			values[0] = sentimentResult.getNegativeWordCount();
            values[1] = sentimentResult.getPositiveWordCount();
            values[2] = negatorResult.getNegatedWordWeight();
            int k = 3;
            for (String word : vector.keySet()) {
                values[k] = vector.get(word);
                k++;
            }

			if (isTestData) {
				testDataGenerator.addValues(values);
			} else {
				trainingDataGenerator.addValues(values);
			}
        }
    }
    
    private ArffFileGenerator createGenerator() {
        ArffFileGenerator generator = new ArffFileGenerator(Paths.get(Constants.PATH_TRAINING_TEST_FILES));
        generator.addNumericAttribute("NumOfNegativeWords");
        generator.addNumericAttribute("NumOfPositiveWords");
        generator.addNumericAttribute("NumOfNegatedWords");

        for (String word : tf_idf.getVocabularyList().keySet()) {
            generator.addNumericAttribute(word);
        }
        
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
