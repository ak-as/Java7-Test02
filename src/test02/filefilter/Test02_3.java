package test02.filefilter;

import java.io.File;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class Test02_3 {

	static final File TARGET_DIR = new File("C:\\pleiades\\workspace\\JavaTest01\\work\\test02\\work_dir");

	public static void main(String[] args) {

		IOFileFilter filter1 = FileFilterUtils.or(FileFilterUtils.directoryFileFilter());
		IOFileFilter filter2 = FileFilterUtils.nameFileFilter("sub_dir2");

		List<File> found = FileFinder.findFiles(TARGET_DIR, filter1, filter2);

		for (File file : found) {
			System.out.println(file.getPath());
		}
	}
}
