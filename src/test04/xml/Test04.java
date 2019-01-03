package test04.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Test04 {

	public static void main(String[] args) throws XPathExpressionException {

		File file = new File("C:\\pleiades\\workspace\\Java7-Test02\\work\\test04\\text.xml");

		Document doc = loadXml(file);

//		Element elm = doc.getDocumentElement();
//		System.out.println(elm.getTextContent());

		XPath xPath = newXPath();

		XPathExpression expr = xPath.compile("/items/item");
		NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			System.out.println(String.format("> %s[name=%s]", node.getNodeName(), node.getAttributes().getNamedItem("name").getNodeValue()));
			NodeList nodes2 = (NodeList)xPath.evaluate("attribute::*", node, XPathConstants.NODESET);
			for (int j = 0; j < nodes2.getLength(); j++) {
				Node node2 = nodes2.item(j);
				System.out.println(String.format("    [%s=%s]", node2.getNodeName(), node2.getNodeValue()));
			}
		}

		System.exit(0);
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

	static XPath newXPath() {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		return xPath;
	}
}
