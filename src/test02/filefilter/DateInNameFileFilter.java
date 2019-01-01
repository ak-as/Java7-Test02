package test02.filefilter;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.AbstractFileFilter;

public class DateInNameFileFilter extends AbstractFileFilter implements Serializable {

	private long cutoff;

	private boolean acceptOlder;

	private Pattern pattern;

	private String groupName;

	private int groupIndex;

	private String dateFormat;

	public DateInNameFileFilter(long cutoff) {
		this(cutoff, true);
	}

	public DateInNameFileFilter(long cutoff, boolean acceptOlder) {
		this.acceptOlder = acceptOlder;
		this.cutoff = cutoff;
	}

	@Override
	public boolean accept(File file) {

		Date date = parseDateFromFilename(file.getName());

		if (date == null) {
			return false;
		}

		boolean newer = date.getTime() > cutoff;
		return acceptOlder ? !newer : newer;
	}

	protected Date parseDateFromFilename(String filename) {

		Date date = null;

		Matcher matcher = pattern.matcher(filename);

		if (matcher.find()) {

			String str = groupName != null && groupName.length() > 0
							? matcher.group(groupName) : matcher.group(groupIndex);

			try {
				date = new SimpleDateFormat(dateFormat).parse(str);
			} catch (ParseException e) {
				//e.printStackTrace();
			}
		}

		return date;
	}

	@Override
	public String toString() {
		String condition = acceptOlder ? "<=" : ">";
		return super.toString() + "(" + condition + cutoff + ")";
	}
}
