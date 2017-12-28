package features;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExclamationMarkFeatureTest {

	private ExclamationMarkFeature feature;
	
	@Before
    public void setUp() throws Exception {
		feature = new ExclamationMarkFeature("exclamationMarks");
    }   
	
    @After
    public void tearDown() throws Exception {
    	feature = null;
    }
	
	@Test
	public void getNoExclamationMark() {
		String input = "This input string contains no exclamation mark";
		Integer result = feature.extract(input);
		assertEquals(new Integer(0), result);
	}
	
	@Test
	public void getExclamationMark() {
		String input = "The movie is good! The actors are better! !!";
		Integer result = feature.extract(input);
		assertEquals(new Integer(4), result);
	}
}
