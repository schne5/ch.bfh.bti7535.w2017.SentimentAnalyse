package features;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NegatorFeatureTest {

	private NegatorFeature feature;
	
	@Before
    public void setUp() throws Exception {
		feature = new NegatorFeature("negator");
    }   
	
    @After
    public void tearDown() throws Exception {
    	feature = null;
    }
	
	@Test
	public void getNoNegation() {
		String input = "The result of this text should be zero";
		String result = feature.extract(input);
		assertEquals(input, result);
	}
	
	@Test
	public void getNegationWithPunctuation() {
		String input = "I don't like this movie , but the actors are good";
		String result = feature.extract(input);
		assertEquals("I don't NOT_like NOT_this NOT_movie , but the actors are good", result);
	}
	
	@Test
	public void getNegationWithBut() {
		String input = "I don't like this movie but the actors are good";
		String result = feature.extract(input);
		assertEquals("I don't NOT_like NOT_this NOT_movie but the actors are good", result);
	}
}
