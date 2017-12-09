package features;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: include brackets as well
public class Negator {
	private static final Logger LOGGER = Logger.getLogger( Negator.class.getName() );
	
	private List<String> negations;
	private List<Character> punctuations;
	
	public Negator() {
		this.negations = new ArrayList<String>(Arrays.asList("n't", "not", "no", "never"));
		this.punctuations =  new ArrayList<Character>(Arrays.asList('.', '.', ',', '?', '!', ';'));
	}
	
	public NegatorResult executeNegation(List<String> input) throws IOException {
		NegatorResult result = new NegatorResult();
		StringBuilder sb = new StringBuilder();
		
		Boolean addNotPrefix = false;
		Boolean isNegation = false;
		Boolean isPunctuation = false;
		
		try {
		    for (String line : input) {
		        String[] words = line.split(" ");
				for (String word : words) {
					if (word.length() == 0) {
						continue;
					}
					
					if (punctuations.contains(word.charAt(word.length() -1))) {
						isPunctuation = true;
						isNegation = false;
						addNotPrefix = false;
					}
					else if (word.endsWith(negations.get(0)) || word.endsWith(negations.get(1))) {
						isPunctuation = false;
						isNegation = true;
						result.incrementTotalWordCount();
					} else {
						result.incrementTotalWordCount();
					}
					
					if (addNotPrefix) {
						sb.append("NOT_");	
						result.incrementNegatedWordCount();
					}
					
					sb.append(word + " ");
					
					addNotPrefix = isNegation && !isPunctuation; 
				}
		    }
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
		
		result.setOutput(sb.toString());
		
		return result;
	}
}
