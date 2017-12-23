package helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * util class for getting and manipulating files
 */
public final class Util {

	private Util(){}

	/**
	 * write time stamp and value to standard output stream
	 * @param value Value to write
	 */
	public static void print(String value) {
		String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
		System.out.format("%s: %s %n", timeStamp, value);
	}

	/**
	 * Returns a List of documents, containing filename, classification and content
	 * @return
	 */
	public static List<Document> getAllDocuments(){
		List<Document> documents = new ArrayList<Document>();
		List<String> filesPositive = readAllFileNames(Constants.PATH_POSITIVE);
		List<String> filesNegative = readAllFileNames(Constants.PATH_NEGATIVE);
		filesPositive.stream().forEach(f -> {
			documents.add(new Document(f,Classification.POSITIVE,getContentFromFile(f,true)));
		});
		filesNegative.stream().forEach(f -> {
			documents.add(new Document(f,Classification.NEGATIVE,getContentFromFile(f,false)));
		});
		return documents;
	}

	/**
	 * Reads all filenames, containing in folder
	 * @param path
	 * @return
	 */
	private static List<String> readAllFileNames(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		List<String> fileNames = new ArrayList<String>();

		Arrays.stream(files).forEach(file->{
			if (file.isFile()) {
				fileNames.add(file.getName());
			}
		});
		return fileNames;
	}

	/**
	 * Returns the content of a file as String
	 * @param filename
	 * @param positive
	 * @return
	 */
	private static String getContentFromFile(String filename, boolean positive) {
		StringBuilder sb = new StringBuilder();
		List<String> lines = null;
		try {
			lines = Files
                    .readAllLines(Paths.get(positive ? Constants.PATH_POSITIVE : Constants.PATH_NEGATIVE, filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lines.stream().forEach(line -> {
			sb.append(line);
		});
		return sb.toString();
	}

	/**
	 * Returns the content of a file as a list of strings
	 * @param file
	 * @return
	 */
		public static List<String> fileToList(Path file) {
			List<String> list = new ArrayList<String>();
			String[] lineArray;
			try {
				List<String> lines = Files.readAllLines(file);
				for (String line : lines) {
					lineArray = (line.split(" "));
					for (String s : lineArray) {
						list.add(s);
					}
				}
			} catch (IOException e) {
				System.out.println("File not readable");
			}
			return list;
		}
	}
