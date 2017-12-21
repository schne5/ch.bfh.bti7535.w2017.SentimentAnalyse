package features;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: include brackets as well
public final class Rating {
	private static final Logger LOGGER = Logger.getLogger( Rating.class.getName() );

	private Rating() {}
	
	public static double getRating(String input) {
		return getRating(input, 1);
	}
	
	public static double getRating(String input, int weight) {
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
			
			if (percent < 0.3) {
				rating = -2 * weight;
			} else if (percent < 0.5) {
				rating = -1 * weight;
			} else if (percent > 0.7) {
				rating = 2 * weight;
			} else if (percent > 0.5) {
				rating = 1 * weight;
			}
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
		
		return rating;
	}
}
