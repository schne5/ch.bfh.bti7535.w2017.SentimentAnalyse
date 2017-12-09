package example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import features.Negator;
import features.NegatorResult;
import helper.Constants;

public class NegatorDemo {

	public static void main(String[] args) throws IOException {
		Negator negator = new Negator();
		
		NegatorResult s1 = negator.executeNegation(Files.readAllLines(Paths.get(Constants.PATH_NEGATIVE, "cv000_29416.txt")));
		NegatorResult s2 = negator.executeNegation(Files.readAllLines(Paths.get(Constants.PATH_NEGATIVE, "cv001_19502.txt")));
		
		System.out.println(s1.getOutput() + "; " + s1.getNegatedWordWeight());
		System.out.println(s2.getOutput() + "; " + s2.getNegatedWordWeight());
	}
}
