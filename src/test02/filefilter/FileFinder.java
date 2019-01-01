package test02.filefilter;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;

public class FileFinder {

	public static List<File> findFiles(File directory, IOFileFilter fileAndDirFilter, IOFileFilter subDirFilter) {
		List<File> files = new LinkedList<>();
		findFiles(files, directory, fileAndDirFilter, subDirFilter);
		return files;
	}

	public static void findFiles(Collection<File> files, File directory, IOFileFilter fileAndDirFilter, IOFileFilter subDirFilter) {
		File[] found = directory.listFiles();
		if (found != null) {
			for (File file : found) {
				if (fileAndDirFilter == null || fileAndDirFilter.accept(file)) {
					files.add(file);
				}
				if (file.isDirectory() && (subDirFilter == null || subDirFilter.accept(file))) {
					findFiles(files, file, fileAndDirFilter, subDirFilter);
				}
			}
		}
	}

}
