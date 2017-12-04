package example;

import java.io.IOException;

import features.Negator;

public class NegatorDemo {

	public static void main(String[] args) throws IOException {
		Negator.process("goldstandard/neg/cv000_29416.txt");
		Negator.process("goldstandard/neg/cv001_19502.txt");
	}
}
