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
        //Trainingsdaten laden
        trainingData = getData("/trainingdata/trainingData.arff");
        //Class Attribut setzen (das letzte)
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        naiveBayes = new NaiveBayes();
        try {
            naiveBayes.buildClassifier(trainingData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        testData = getData("/testdata/testData.arff");
        testData.setClassIndex(testData.numAttributes() - 1);
        for(int i=0; i<testData.numInstances(); i++) {
            System.out.println(testData.instance(i));
            double index = 0;
            try {
                index = naiveBayes.classifyInstance(testData.instance(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String className = trainingData.classAttribute().value((int)index);
            System.out.println(className);
        }
    }

    public static Instances getData(String fileName) {
        BufferedReader reader = null;
        Instances data = null;
        try {
            reader = new BufferedReader(
                    new FileReader(fileName));
            data = new Instances(reader);
            reader.close();
        } catch (IOException e) {
        }
        return data;
    }
}
