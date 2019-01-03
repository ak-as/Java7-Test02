package test07.findfiles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Test07 {

	static final File INPUT_FILE = new File("C:\\pleiades\\workspace\\Java7-Test02\\work\\test07\\test.xml");

	public static void main(String[] args) {

		try (InputStream is = Files.newInputStream(INPUT_FILE.toPath(), StandardOpenOption.READ)) {
			new FileCleaner().execute(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
