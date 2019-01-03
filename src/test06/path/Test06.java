package test06.path;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

public class Test06 {

	public static void main(String[] args) {

		FileSystem fs = FileSystems.getDefault();
		PathMatcher glob = fs.getPathMatcher("glob:*.java");
		PathMatcher regex = fs.getPathMatcher("regex:(?i).*\\.JAVA$");

		String[] a = { "a.java", "a.Java", "a.JAVA", "a.class", };

		for (String name : a) {
			Path path = Paths.get(name);
			System.out.printf("%-8s %-5s %-5s%n", path, glob.matches(path), regex.matches(path));
		}
	}
}
