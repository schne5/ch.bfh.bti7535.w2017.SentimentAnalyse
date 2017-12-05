package helper;

import features.SentimentWordCountResult;
import features.SentimentWordCounter;
import weka.core.Instance;

import java.io.File;
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
        ArffFileGenerator trainingDataGenerator = createGenerator();
        ArffFileGenerator testDataGenerator = createGenerator();

        List<String> filesPositive = readAllFiles(PATH_POSITIVE);
        List<String> filesNegative = readAllFiles(PATH_NEGATIVE);

        insertFeature(trainingDataGenerator, sentimentWordCounter, filesPositive, true, 0, 500, false);
        insertFeature(trainingDataGenerator, sentimentWordCounter, filesNegative, false, 0, 500, false);

        insertFeature(testDataGenerator, sentimentWordCounter, filesPositive, true, 500, 600, true);
        insertFeature(testDataGenerator, sentimentWordCounter, filesNegative, false, 500, 600, true);

        trainingDataGenerator.generateFile(FILE_NAME_TRAINING);
        testDataGenerator.generateFile(FILE_NAME_TEST);
    }

    private static void insertFeature(ArffFileGenerator generator, SentimentWordCounter sentimentWordCounter, List<String> files, boolean positive, int rangeFrom, int rangeTo, boolean isTestData) {
        for (int i = rangeFrom; i < rangeTo; i++) {
            SentimentWordCountResult result = sentimentWordCounter.countSentimentWords(Paths.get(positive ? PATH_POSITIVE : PATH_NEGATIVE, files.get(i)));
            generator.addValues(new double[]{result.getNegativeWordCount(), result.getPositiveWordCount(), isTestData ? Instance.missingValue() : positive ? 1 : 0});
        }
    }
    private static ArffFileGenerator createGenerator() {
        ArffFileGenerator generator = new ArffFileGenerator(Paths.get(PATH_TRAINING_TEST_FILES));
        generator.addNumericAttribute("NumOfNegativeWords");
        generator.addNumericAttribute("NumOfPositiveWords");
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
