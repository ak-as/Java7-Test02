package test07.findfiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileCleaner {

	static class Parameters {

		File directory;

		IOFileFilter fileFilter;

		IOFileFilter subDirectoryFilter;
	}

	protected StringSubstitutor[] substitutors;

	public FileCleaner() {
		init();
	}

	public void execute(InputStream is) {

		List<File> targetFiles = new LinkedList<File>();

		init();

		List<Map<String, Object>> entries = createEntries(is);

//		for (Map<String, Object> entry : entries) {
//			System.out.println();
//			System.out.println("> entry");
//			for (Map.Entry<String, Object> e : entry.entrySet()) {
//				System.out.println("    " + e.getKey() + "=" + e.getValue());
//			}
//		}

		resolveVariableInEntries(entries);

//		for (Map<String, Object> entry : entries) {
//			System.out.println();
//			System.out.println("> entry");
//			for (Map.Entry<String, Object> e : entry.entrySet()) {
//				System.out.println("    " + e.getKey() + "=" + e.getValue());
//			}
//		}

		for (Map<String, Object> entry : entries) {

			String disabled = MapUtils.getString(entry, "disabled");
			if (disabled != null && (disabled.equals("1") || disabled.equals("true"))) {
				continue;
			}

			Parameters params = createParameters(entry);

			targetFiles.clear();
			findFiles(targetFiles, params.directory, params.fileFilter, params.subDirectoryFilter);

			for (File targetFile : targetFiles) {
				deleteFile(targetFile);
			}
		}
	}

	protected void init() {

		StringLookup[] lookups = {
				StringLookupFactory.INSTANCE.interpolatorStringLookup(),
				StringLookupFactory.INSTANCE.resourceBundleStringLookup()
			};

		this.substitutors = new StringSubstitutor[lookups.length];
		for (int i = 0; i < lookups.length; i++) {
			this.substitutors[i] = new StringSubstitutor(lookups[i]);
		}
	}

	private List<Map<String, Object>> createEntries(InputStream is) {

		List<Map<String, Object>> entries = new ArrayList<Map<String, Object>>();

		try {

			Document doc = loadXml(is);
			XPath xPath = newXPath();
			XPathExpression attrsExpr = xPath.compile("attribute::*");

			NodeList entryNodes = (NodeList)xPath.evaluate("/entries/entry", doc, XPathConstants.NODESET);

			for (int i = 0; i < entryNodes.getLength(); i++) {

				NodeList attrNodes = (NodeList)attrsExpr.evaluate(entryNodes.item(i), XPathConstants.NODESET);

				Map<String, Object> entry = new CaseInsensitiveMap<String, Object>();
				for (int j = 0; j < attrNodes.getLength(); j++) {
					Node attrNode = attrNodes.item(j);
					entry.put(attrNode.getNodeName(), attrNode.getNodeValue());
				}

				entries.add(entry);
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return entries;
	}

	private Document loadXml(InputStream is) {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(is);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	private XPath newXPath() {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		return xPath;
	}

	private void resolveVariableInEntries(List<Map<String, Object>> entries) {
		for (Map<String, Object> entry : entries) {
			for (Map.Entry<String, Object> e : entry.entrySet()) {
				Object value = e.getValue();
				if (value instanceof CharSequence) {
					CharSequence cs = (CharSequence)value;
					if (cs.length() >= 4) {
						String newValue = resolveVariable(cs.toString());
						e.setValue(newValue);
					}
				}
			}
		}
	}

	private String resolveVariable(String str) {

		if (str == null || str.length() < 4) {
			return str;
		}

		String str2 = str;
		for (int i = 0; i < this.substitutors.length; i++) {
			if (this.substitutors[i] != null) {
				str2 = this.substitutors[i].replace(str2);
			}
		}

		return str2;
	}

	private Parameters createParameters(Map<String, Object> entry) {



		String directory = MapUtils.getString(entry, "directory");

		boolean recursive = MapUtils.getBoolean(entry, "recursive");

		String file_filter = MapUtils.getString(entry, "file-filter");

		String directory_filter = MapUtils.getString(entry, "directory-filter");

		String date_pattern = MapUtils.getString(entry, "date-pattern");

		String date_format = MapUtils.getString(entry, "date-format");

		String group_index = MapUtils.getString(entry, "group-index");

		Parameters params = new Parameters();
		params.directory = new File(directory);
		params.subDirectoryFilter = recursive ? FileFilterUtils.trueFileFilter() : FileFilterUtils.falseFileFilter();

		return null;
	}

	private void findFiles(Collection<File> foundFiles, File directory, IOFileFilter filter, IOFileFilter subDirFilter) {
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (filter == null || filter.accept(file)) {
					foundFiles.add(file);
				}
				if (file.isDirectory() && (subDirFilter == null || subDirFilter.accept(file))) {
					findFiles(foundFiles, file, filter, subDirFilter);
				}
			}
		}
	}

	protected void deleteFile(File file) {
		try {
			if (file.exists()) {
				FileUtils.forceDelete(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
