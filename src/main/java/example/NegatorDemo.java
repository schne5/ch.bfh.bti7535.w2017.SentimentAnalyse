package example;

import java.io.File;
import java.io.IOException;

import features.Negator;
import features.NegatorResult;

public class NegatorDemo {

	public static void main(String[] args) throws IOException {
		Negator negator = new Negator();
		
		NegatorResult s1 = negator.executeNegation(new File("goldstandard/neg/cv000_29416.txt"));
		NegatorResult s2 = negator.executeNegation(new File("goldstandard/neg/cv001_19502.txt"));
		
		System.out.println(s1.getOutput() + "; " + s1.getNegatedWordPercentage());
		System.out.println(s2.getOutput() + "; " + s2.getNegatedWordPercentage());
	}
}
