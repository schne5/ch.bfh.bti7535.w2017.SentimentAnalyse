package features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: include brackets as well
// TODO: can we still use the negated text, or do we use the count only?
public final class NegationFeature extends Feature<String> {
	private static final Logger LOGGER = Logger.getLogger( NegationFeature.class.getName() );
	
	private static List<String> negations = new ArrayList<String>(Arrays.asList("n't", "not", "no", "never"));;
	private static List<String> punctuations = new ArrayList<String>(Arrays.asList(".", ".", ",", "?", "!", ";", "but"));
	
	public NegationFeature(String name) {
		super(name, false);
	}
	
	public String extract(String input) {
		StringBuilder sb = new StringBuilder();
		
		Boolean addNotPrefix = false;
		Boolean isNegation = false;
		Boolean isPunctuation = false;
		
		int totalWordsCount = 0;
		int negatedWordsCount = 0;
		
		try {	    
	        String[] words = input.split(" ");
			for (String word : words) {
				if (word.length() == 0) {
					continue;
				}
				
				if (punctuations.contains(word)) {
					isPunctuation = true;
					isNegation = false;
					addNotPrefix = false;
				}
				else if (word.endsWith(negations.get(0)) || word.endsWith(negations.get(1))) {
					isPunctuation = false;
					isNegation = true;
					totalWordsCount++;
				} else {
					totalWordsCount++;
				}
				
				if (addNotPrefix) {
					sb.append("NOT_");	
					negatedWordsCount++;
				}
				
				sb.append(word + " ");
				
				addNotPrefix = isNegation && !isPunctuation; 
			}
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
		
		return sb.toString().trim();
		
		//return (double)negatedWordsCount / (totalWordsCount == 0 ? 1 : totalWordsCount);
	}
}
