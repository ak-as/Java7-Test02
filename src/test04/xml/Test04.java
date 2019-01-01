package test04.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Test04 {

	public static void main(String[] args) {

		File file = new File("C:\\pleiades\\workspace\\JavaTest01\\work\\test04\\text.xml");

		Document doc = loadXml(file);
		Element elm = doc.getDocumentElement();


		System.out.println(elm.getTextContent());
	}

	static Document loadXml(File file) {
		Document document = null;
		try (InputStream is = new FileInputStream(file)) {
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
}
