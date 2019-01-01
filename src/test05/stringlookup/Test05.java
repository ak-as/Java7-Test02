package test05.stringlookup;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;

public class Test05 {

	public static void main(String[] args) {

		StringLookup slkup = StringLookupFactory.INSTANCE.interpolatorStringLookup();
		StringSubstitutor s = new StringSubstitutor(slkup);

		StringLookup lookup2 = StringLookupFactory.INSTANCE.resourceBundleStringLookup();
		StringSubstitutor s2 = new StringSubstitutor(lookup2);

		println(s, "${env:HOMEPATH}");
		println(s, "${sys:user.dir}");
		println(s, "${sys:file.encoding}");
		println(s, "${date:yyyy-MM-dd HH:mm:ss.SSS}");
		println(s2, "${application:app.path}");

	}

	static void println(StringSubstitutor s, String str) {

		System.out.println(str + "=" + s.replace(str) + "");
	}
}
