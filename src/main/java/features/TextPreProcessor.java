package features;

import java.util.List;

public class TextPreProcessor {
    public static String removeStopWords(String text){
        return StopWordElimination.removeStopWordsFromText(text);
    }
    public List<String> increaseWordWeight(int factor, double textSection,List<String> reviewText){
        int length = reviewText.size();
        int startIndex = (int)textSection *length;
        List<String> list = reviewText.subList(startIndex,length-1);
        for(int i=0;i<factor;i++)
            reviewText.addAll(list);
        return reviewText;
    }
}
