package helper;

import java.util.*;

public class WordStatistik {
    public static HashMap<String, Integer> countWords(String text, boolean uppercase) {
        StringTokenizer tokenizer = new StringTokenizer(text, "\n\"-+,&%$§.;:?!(){}[]1234567890*@ ");
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>();

        // a first draft of adding more weight to words
        HashMap<String, Integer> intensifications = new HashMap<String, Integer>();
        intensifications.put("VERY", 1);
        intensifications.put("MORE", 1);
        intensifications.put("MOST", 2);
        intensifications.put("LESS", 1);
        intensifications.put("LEAST", -2);
        intensifications.put("ALWAYS", 3);
        intensifications.put("NEVER", -3);
        intensifications.put("TOO", 1);

        String preword = "";

        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();

            int weight = intensifications.containsKey(preword) ? intensifications.get(preword) : 0;

            if (uppercase)
                word = word.toUpperCase();
            Integer count = wordMap.get(word);
            if (count == null) {
                wordMap.put(word, new Integer(1));
            } else {
                int countValue = count.intValue() + 1 + weight;
                wordMap.put(word, new Integer(countValue));
            }

            preword = word.toUpperCase();
        }
        return wordMap;
    }

    /**
     * returns a list of words for a list of lines
     *
     * @param lines
     * @return
     */
    public static List<String> getWords(List<String> lines) {
        List<String> words = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(splitText(lines), "\n\"-+,&%$§.;:?!(){}[]1234567890*@ ");
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            words.add(word);
        }
        return words;
    }

    /**
     * returns a text with a list of lines as param
     *
     * @param lines
     * @return
     */
    public static String splitText(List<String> lines) {
        StringBuilder text = new StringBuilder();
        lines.stream().forEach(line -> {
            text.append(line);
        });
        return text.toString();
    }

    /**
     * returns a list of words with text as param
     *
     * @param txt
     * @return
     */
    public static List<String> getWords(String txt) {
        List<String> words = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(txt, "\n\"-+,&%$§.;:?!(){}[]1234567890*@ ");
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            words.add(word);
        }
        return words;
    }

    public static HashMap<String, Integer> getVocabularyList(List<Document> docs, boolean classification) {
        HashMap<String, Integer> negative = new HashMap<>();
        HashMap<String, Integer> positive = new HashMap<>();

        //generates the vocabulary lists
        docs.stream().forEach(d -> {
            getWords(d.getContent()).stream().forEach(w -> {
                if (d.getGold() == Classification.NEGATIVE) {
                    addWord(negative, w);
                } else {
                    addWord(positive, w);
                }
            });
        });
        //removes all the words which are present in both classes
        for (Iterator<String> iterator = negative.keySet().iterator(); iterator.hasNext(); ) {
            String word = iterator.next();
            if (positive.containsKey(word) || negative.get(word) <= 3) {
                iterator.remove();
                positive.remove(word);
            }
        }
        //removes all the words which are present in both classes
        for (Iterator<String> iterator = positive.keySet().iterator(); iterator.hasNext(); ) {
            String word = iterator.next();
            if (positive.get(word) <= 3) {
                iterator.remove();

            }
        }
        return classification ? positive : negative;
    }

    private static void addWord(HashMap<String, Integer> words, String word) {
        if (words.containsKey(word)) {
            int value = words.get(word);
            value++;
            words.put(word, value);
        } else {
            words.put(word, 1);
        }
    }
}
