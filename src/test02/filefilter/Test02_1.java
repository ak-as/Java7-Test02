package test02.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class Test02_1 {

	static final File targetDir = new File("C:\\pleiades\\workspace\\JavaTest01\\work\\test02\\work_dir");

	public static void main(String[] args) {

		try {

//			filterList__null(); // ⇒ 例外発生

			filterList__true(); // ⇒ 直下のファイルとディレクトリ

			filterList__false(); // ⇒ なし

			listFiles__true__null(); // ⇒ 直下のファイルのみ

			listFiles__true__true(); // ⇒ ファイルのみ（再帰的に）

			listFiles__true__false(); // ⇒ 直下のファイルのみ

			listFiles__false__null(); // ⇒ なし

			listFiles__false__true(); // ⇒ なし

			listFiles__false__false(); // ⇒ 直下のファイルのみ

			listFilesAndDirs__true__null(); // ⇒ targetDirと直下のファイルのみ

			listFilesAndDirs__true__true(); // ⇒ ファイルとディレクトリ（再帰的に）

			listFilesAndDirs__true__false(); // ⇒ targetDirと直下のファイルのみ

			listFilesAndDirs__false__null(); // ⇒ targetDirのみ

			listFilesAndDirs__false__true(); // ⇒ ディレクトリのみ（再帰的に）

			listFilesAndDirs__false__false(); // ⇒ targetDirのみ

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static Collection listFiles(File dir, IOFileFilter fileFilter, IOFileFilter dirFilter, boolean recursive) throws IOException {

		Collection<File> files = null;

		if (recursive) {

			IOFileFilter filters = FileFilterUtils.or(fileFilter, dirFilter);
			File[] found = dir.listFiles((FileFilter)filters);
			files = new LinkedList<File>(Arrays.asList(found));

		} else {

			files = FileUtils.listFiles(dir, fileFilter, dirFilter);
		}

		return files;
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

	static void filterList__null() throws IOException {

		IOFileFilter filter = null;//FileFilterUtils.falseFileFilter();

		List<File> files = FileFilterUtils.filterList(filter, targetDir.listFiles());

		print_result("filterList(null, targetDir);", files);
	}

	static void filterList__true() throws IOException {

		IOFileFilter filter = FileFilterUtils.trueFileFilter();

		List<File> files = FileFilterUtils.filterList(filter, targetDir.listFiles());

		print_result("filterList(true, targetDir);", files);
	}

	static void filterList__false() throws IOException {

		IOFileFilter filter = FileFilterUtils.falseFileFilter();

		List<File> files = FileFilterUtils.filterList(filter, targetDir.listFiles());

		print_result("filterList(false, targetDir);", files);
	}

	static void listFiles__true__null() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
		IOFileFilter dirFilter  = null;

		Collection<File> files = FileUtils.listFiles(targetDir, fileFilter, dirFilter);

		print_result("listFiles(target, true, null);", files);
	}

	static void listFiles__true__true() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
		IOFileFilter dirFilter  = FileFilterUtils.trueFileFilter();

		Collection<File> files = FileUtils.listFiles(targetDir, fileFilter, dirFilter);

		print_result("listFiles(target, true, true);", files);
	}

	static void listFiles__true__false() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
		IOFileFilter dirFilter  = FileFilterUtils.falseFileFilter();

		Collection<File> files = FileUtils.listFiles(targetDir, fileFilter, dirFilter);

		print_result("listFiles(target, true, false);", files);
	}

	static void listFiles__false__null() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.falseFileFilter();
		IOFileFilter dirFilter  = null;

		Collection<File> files = FileUtils.listFiles(targetDir, fileFilter, dirFilter);

		print_result("listFiles(target, false, null);", files);
	}

	static void listFiles__false__true() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.falseFileFilter();
		IOFileFilter dirFilter  = FileFilterUtils.trueFileFilter();

		Collection<File> files = FileUtils.listFiles(targetDir, fileFilter, dirFilter);

		print_result("listFiles(target, false, true);", files);
	}

	static void listFiles__false__false() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
		IOFileFilter dirFilter  = FileFilterUtils.falseFileFilter();

		Collection<File> files = FileUtils.listFiles(targetDir, fileFilter, dirFilter);

		print_result("listFiles(target, false, false);", files);
	}

	static void listFilesAndDirs__true__null() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
		IOFileFilter dirFilter  = null;

		Collection<File> files = FileUtils.listFilesAndDirs(targetDir, fileFilter, dirFilter);

		print_result("listFilesAndDirs(target, true, null);", files);
	}

	static void listFilesAndDirs__true__true() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
		IOFileFilter dirFilter  = FileFilterUtils.trueFileFilter();

		Collection<File> files = FileUtils.listFilesAndDirs(targetDir, fileFilter, dirFilter);

		print_result("listFilesAndDirs(target, true, true);", files);
	}

	static void listFilesAndDirs__true__false() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
		IOFileFilter dirFilter  = FileFilterUtils.falseFileFilter();

		Collection<File> files = FileUtils.listFilesAndDirs(targetDir, fileFilter, dirFilter);

		print_result("listFilesAndDirs(target, true, false);", files);
	}

	static void listFilesAndDirs__false__null() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.falseFileFilter();
		IOFileFilter dirFilter  = null;

		Collection<File> files = FileUtils.listFilesAndDirs(targetDir, fileFilter, dirFilter);

		print_result("listFilesAndDirs(target, false, null);", files);
	}

	static void listFilesAndDirs__false__true() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.falseFileFilter();
		IOFileFilter dirFilter  = FileFilterUtils.trueFileFilter();

		Collection<File> files = FileUtils.listFilesAndDirs(targetDir, fileFilter, dirFilter);

		print_result("listFilesAndDirs(target, false, true);", files);
	}

	static void listFilesAndDirs__false__false() throws IOException {

		IOFileFilter fileFilter = FileFilterUtils.falseFileFilter();
		IOFileFilter dirFilter  = FileFilterUtils.falseFileFilter();

		Collection<File> files = FileUtils.listFilesAndDirs(targetDir, fileFilter, dirFilter);

		print_result("listFilesAndDirs(target, false, false);", files);
	}
}
