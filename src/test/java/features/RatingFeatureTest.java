package features;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RatingFeatureTest {

	private RatingFeature feature;
	
	@Before
    public void setUp() throws Exception {
		feature = new RatingFeature("rating");
    }   
	
    @After
    public void tearDown() throws Exception {
    	feature = null;
    }
	
	@Test
	public void getNoRating() {
		String input = "This string contains no rating";
		Double result = feature.extract(input);
		assertEquals(new Double(0), result);
	}
	
	@Test
	public void getNegativeRating() {
		String input = "Movie is 2/10, actors are 3/10.";
		Double result = feature.extract(input);
		assertEquals(new Double(-1), result);
	}
	
	@Test
	public void getNeutralRating() {
		String input = "Movie is 4/10, actors are 5/10.";
		Double result = feature.extract(input);
		assertEquals(new Double(0), result);
	}
	
	@Test
	public void getPositiveRating() {
		String input = "Movie is 7/10, actors are 8/10.";
		Double result = feature.extract(input);
		assertEquals(new Double(1), result);
	}
}
