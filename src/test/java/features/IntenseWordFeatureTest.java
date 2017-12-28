package features;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IntenseWordFeatureTest {

	private IntenseWordFeature feature;
	
	@Before
    public void setUp() throws Exception {
		feature = new IntenseWordFeature("intenseWords");
    }   
	
    @After
    public void tearDown() throws Exception {
    	feature = null;
    }
	
	@Test
	public void getNoIntenseWords() {
		String input = "This input string contains no intense words";
		Integer result = feature.extract(input);
		assertEquals(new Integer(0), result);
	}
	
	@Test
	public void getIntenseWords() {
		String input = "This movie was very good. I will never forget the wonderful story told.";
		Integer result = feature.extract(input);
		assertEquals(new Integer(3), result);
	}
}
