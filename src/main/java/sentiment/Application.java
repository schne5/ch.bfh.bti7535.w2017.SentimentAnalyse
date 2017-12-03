package sentiment;

import features.SentimentWordCountResult;
import features.SentimentWordCounter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    public static void main(String[] args){

        //test SentimentCounter
        Path path = Paths.get("goldstandard\\pos", "cv000_29590.txt");
        SentimentWordCountResult result = new SentimentWordCounter().countSentimentWords(path);
        System.out.println("Negative: "+result.getNegativeWordCount());
        System.out.println("Positive: "+result.getPositiveWordCount());
    }
}
