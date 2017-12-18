package helper;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.stemmers.LovinsStemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class NaiveBayesClassifier {
    private FilteredClassifier filteredBayes = new FilteredClassifier();
    private Instances data = null;

    public void setup(){
        readArffFile();
        StringToWordVector vectorFilter = getSTWVFilter();
        try
        {
            vectorFilter.setInputFormat(this.data);
        } catch(Exception e) {
            e.printStackTrace();
        }
        //Combine filters
        MultiFilter multiFilter = new MultiFilter();
        Filter[] filters = new Filter[1];
        filters[0] = vectorFilter;
        //filters[1] =getASFilter();
        multiFilter.setFilters(filters);

        this.filteredBayes.setClassifier(new weka.classifiers.bayes.NaiveBayes());
        this.filteredBayes.setFilter(multiFilter);
        try
        {
            multiFilter.setInputFormat(this.data);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void crossValidate() throws Exception {
        Evaluation eval = new Evaluation(data);
        for (int n = 0; n < 10; n++) {
            Instances train = data.trainCV(10, n);
            Instances test = data.testCV(10, n);

                filteredBayes.buildClassifier(train);
            eval.evaluateModel(filteredBayes, test);
            double correctRate = eval.pctCorrect();
            System.out.println(eval.pctCorrect());
        }
    }

    private  void  readArffFile() {
        try
        {
            //Read arff file
            BufferedReader reader = new BufferedReader(new FileReader(Constants.PATH_RESSOURCES+"\\"+Constants.FILE_NAME_REVIEW));
            ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
            this.data = arff.getData();
            this.data.setClassIndex(data.numAttributes() - 2);
            reader.close();
        } catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static NGramTokenizer getNgramTokenizer(int maxSize) {
        NGramTokenizer ngram = new NGramTokenizer();
        ngram.setNGramMinSize(1);
        ngram.setNGramMaxSize(maxSize);
        return ngram;
    }

    private static StringToWordVector getSTWVFilter() {
        StringToWordVector filter = new StringToWordVector();

        //filter options
        filter.setWordsToKeep(50000);
        filter.setLowerCaseTokens(true);
        filter.setMinTermFreq(2);
        LovinsStemmer stem = new LovinsStemmer();
        filter.setStemmer(stem);
        //filter.setStopwords(new File("resources/stopwords.txt"));
        filter.setTokenizer(getNgramTokenizer(3));
        return filter;
    }
    private static AttributeSelection getASFilter() {
        AttributeSelection filter = new AttributeSelection();

        InfoGainAttributeEval ev = new InfoGainAttributeEval();
        Ranker ranker = new Ranker();
        ranker.setNumToSelect(1000);

        filter.setEvaluator(ev);
        filter.setSearch(ranker);

        return filter;
    }
}
