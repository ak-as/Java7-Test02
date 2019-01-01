package test02.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class Test02_2 {

	static final File targetDir = new File("C:\\pleiades\\workspace\\JavaTest01\\work\\test02\\work_dir");

	public static void main(String[] args) {

		Collection<File> found = null;

		try {

			found = findFiles(targetDir, FileFilterUtils.trueFileFilter());
			print_result("findFiles", found);

			found = findFilesRecursively(targetDir, FileFilterUtils.trueFileFilter(), FileFilterUtils.trueFileFilter());
			print_result("findFilesRecursively", found);

			found = findDirs(targetDir, FileFilterUtils.trueFileFilter());
			print_result("findDirs", found);
			print_result("findFiles", found);

			found = findDirsRecursively(targetDir, FileFilterUtils.trueFileFilter());
			print_result("findDirsRecursively", found);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static Collection<File> findFiles(File dir, IOFileFilter fileFilter) throws IOException {

		Collection<File> files = new LinkedList<File>();

		IOFileFilter effFileFilter = FileFilterUtils.and(fileFilter, FileFilterUtils.fileFileFilter());
		File[] found = dir.listFiles((FileFilter)effFileFilter);

		if (found != null) {
			for (File f : found) {
				files.add(f);
			}
		}

		return files;
	}

	static Collection<File> findFilesRecursively(File dir, IOFileFilter fileFilter, IOFileFilter dirFilter) throws IOException {
		return FileUtils.listFiles(dir, fileFilter, dirFilter);
	}

	static Collection<File> findDirs(File dir, IOFileFilter dirFilter) throws IOException {

		Collection<File> dirs = new LinkedList<File>();

		IOFileFilter effDirFilter = FileFilterUtils.and(dirFilter, FileFilterUtils.directoryFileFilter());
		File[] found = dir.listFiles((FileFilter)effDirFilter);

		if (found != null) {
			for (File f : found) {
				dirs.add(f);
			}
		}

		return dirs;
	}

	static Collection<File> findDirsRecursively(File dir, IOFileFilter dirFilter) throws IOException {
		Collection<File> dirs = FileUtils.listFilesAndDirs(dir, FileFilterUtils.falseFileFilter(), dirFilter);
		((List<File>)dirs).remove(dir);
		return dirs;
	}

	static void print_result(String title, Collection<File> files) throws IOException {

		System.out.println();
		System.out.println("##################################################");
		System.out.println();
		System.out.println(" " + title);
		System.out.println();
		System.out.println("##################################################");
		System.out.println();

		for (File f : files) {
			System.out.println(f.getCanonicalPath());
		}
	}

}
