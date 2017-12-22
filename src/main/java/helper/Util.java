package helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	private static List<String> readAllFileNames(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		List<String> fileNames = new ArrayList<String>();

		for (File file : files) {
			if (file.isFile()) {
				fileNames.add(file.getName());
			}
		}
		return fileNames;
	}

	private static String getContentFromFile(String filename, boolean positive) {
		StringBuilder sb = new StringBuilder();
		List<String> lines = null;
		try {
			lines = Files
                    .readAllLines(Paths.get(positive ? Constants.PATH_POSITIVE : Constants.PATH_NEGATIVE, filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String line : lines) {
			sb.append(line);
		}
		return sb.toString();
	}
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
