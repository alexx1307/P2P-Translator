package peers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * Klasa reprezentujaca tlumaczony dokument.
 * 
 * @author lukasz
 * 
 */
public class DocumentFile {
	private int position;
	private int length;
	private int firstLength;
	private int lineNumber;
	private String filename;
	private LinkedList<String> original;
	private LinkedList<String> translated;
	private TreeMap<Integer, LinkedList<String>> chunks;

	public DocumentFile(String filename) {
		this.filename = filename;
		original = new LinkedList<>();
		translated = new LinkedList<>();
		chunks = new TreeMap<>();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(new File(filename)));

			String line;
			while ((line = br.readLine()) != null) {
				original.add(line);
			}
			br.close();
		} catch (IOException e) {
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
		lineNumber = original.size();
	}

	public void prepareChunks(int translatorsNumber) {
		position = 0;
		length = lineNumber / translatorsNumber;
		firstLength = length + (lineNumber % translatorsNumber);
	}

	public LinkedList<String> getNextChunk() {
		int l;
		if (firstLength >= 0) {
			l = firstLength;
			firstLength = -1;
		} else {
			l = length;
		}
		LinkedList<String> result = new LinkedList<>();
		for (int i = position; i < position + l; i++)
			result.add(original.get(i));
		position += l;
		return result;
	}

}
