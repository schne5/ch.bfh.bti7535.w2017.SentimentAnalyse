package helper;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NaiveBayesClassifier {
    Classifier naiveBayes = null;
    Instances trainingData = null;
    Instances testData = null;

    public void train() {
        try {
            //Trainingsdaten laden
            trainingData = getData(Constants.PATH_TRAINING_TEST_FILES + "/" + Constants.FILE_NAME_TRAINING);
            //Class Attribut setzen (das letzte)
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
            naiveBayes = new NaiveBayes();
            naiveBayes.buildClassifier(trainingData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        try {
            int fehler =0;
            testData = getData(Constants.PATH_TRAINING_TEST_FILES + "/" + Constants.FILE_NAME_TEST);
            testData.setClassIndex(testData.numAttributes() - 1);
            for (int i = 0; i < testData.numInstances(); i++) {
                System.out.println(testData.instance(i));
                double index = 0;

                index = naiveBayes.classifyInstance(testData.instance(i));
                String className = trainingData.classAttribute().value((int) index);
                if(index > 0 && i >= testData.numInstances() /2 )
                    fehler ++;
                if(index == 0 && i < testData.numInstances() /2 )
                    fehler ++;
                System.out.println(className);
            }
            System.out.println("Anzahl Fehler: " + fehler);
            System.out.println("Anzahl Total: " + testData.numInstances());
            System.out.println("Richtig: " +((double)(((double)testData.numInstances()-(double)fehler)/(double)testData.numInstances()) * (double)100) + "%");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static Instances getData(String fileName) throws IOException {
        BufferedReader reader = null;
        Instances data = null;
        try {
            reader = new BufferedReader(
                    new FileReader(fileName));
            data = new Instances(reader);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return data;
    }
}
