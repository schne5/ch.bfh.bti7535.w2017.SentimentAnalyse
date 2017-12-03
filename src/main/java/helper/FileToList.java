package helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @autor Anna
 * 
 * @since 03.12.2017
 * 
 *        Liest ein File ein und gibt den Inhalt in Form einer Liste zurück
 *        (splittet by Leerschlägen -> die Satzzeichen sind auch in der Liste)
 */
public class FileToList {
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
