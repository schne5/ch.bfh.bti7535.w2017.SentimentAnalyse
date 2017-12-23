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
		Double result = feature.get(input);
		assertEquals(new Double(0), result);
	}
	
	@Test
	public void getNegation() {
		String input = "I don't like this movie , but the actors are good";
		Double result = feature.get(input);
		assertEquals(new Double(0.3), result);
	}
}
