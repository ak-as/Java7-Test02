package test09.jexl;

import java.io.File;
import java.net.URL;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang3.time.DateUtils;

public class Test09 {

	// https://commons.apache.org/proper/commons-jexl/reference/syntax.html
	// http://commons.apache.org/proper/commons-jexl/reference/examples.html

	static class innerFoo {
		Object bar() {
			return 1 + 2;
		}
	}

	public static void main(String[] args) {
		try {
			URL url = new File("C:\\pleiades\\workspace\\Java7-Test02\\src\\test09\\jexl\\script01.txt").toURI().toURL();
			runScript(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	static void test01() {

//		String jexlExp = "DateUtils.parseDate('20160205', 'yyyyMMdd')";
		String jexlExp = "java.lang.Math.abs(-1.5)";

		// Create or retrieve an engine
		JexlEngine jexl = new JexlBuilder().create();

		// Create an expression
		JexlExpression e = jexl.createExpression(jexlExp);

		// Create a context and add data
		JexlContext jc = new MapContext();
		jc.set("System", System.class);
		jc.set("Class", Class.class);
		jc.set("DateUtils", DateUtils.class);

		// Now evaluate the expression, getting the result
		Object o = e.evaluate(jc);
		System.out.println(o);
	}

	static void runScript(URL url) {

		JexlEngine engine = new JexlBuilder().create();
		JexlScript script = engine.createScript(url);

		JexlContext jc = new MapContext();
		jc.set("System", System.class);
		jc.set("Class", Class.class);
		jc.set("DateUtils", DateUtils.class);

		Object result = script.execute(jc);
		System.out.println(result);
	}
}
