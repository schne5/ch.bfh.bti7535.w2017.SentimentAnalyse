package features;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Feature for analyzing rating in text
 */
public final class RatingFeature extends Feature<Double> {
	
	private static final Logger LOGGER = Logger.getLogger( RatingFeature.class.getName() );
	
	public RatingFeature(String name) {
		this(name, 1);
	}
	
	public RatingFeature(String name, int weight) {
		super(name, weight);
	}
	
	public Double get(String input) {
		List<Integer> ratings = new ArrayList<Integer>();
		double rating = 0.5;
		
		try {	    
	        String[] words = input.split(" ");
			for (String word : words) {
				if (word.length() == 0) {
					continue;
				}
				
				int index = word.indexOf("/10");
				if (index < 0) {
					continue;
				}
				
				try {
					String ratingString = word.substring(0, index);
					int ratingInt = Integer.parseInt(ratingString);
					ratings.add(ratingInt);
				} catch (NumberFormatException e) {
					continue;
				}
			}
			
			double percent = 0;
			for (int r : ratings) {
				percent += r;
			}
			percent = percent / (ratings.size() * 10);
			
			if (percent < 0.35) { // 0-35% => negative rating
				rating = -1 * this.getWeight();
			} else if (percent > 0.65) { // 65-100% => positive rating
				rating = 1 * this.getWeight();
			} else { // 35-65% => neutral rating
				rating = 0;
			}
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
		return rating;
	}
}
