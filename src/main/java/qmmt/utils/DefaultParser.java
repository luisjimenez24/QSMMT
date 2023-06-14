package qmmt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DefaultParser {

	private File file;
	private int mutantCounter = 0;

	public DefaultParser(File file) {
		this.file = file;
	}

	private File getFile() {
		return this.file;
	}

	public Document buildDocument() {
		FileInputStream fileIS;
		try {
			fileIS = new FileInputStream(this.getFile());
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(fileIS);
			return xmlDocument;
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
			return null;
		}
	}

	public NodeList evaluateExpresion(Document umlDocument, String expression) {
		NodeList nodeList = null;
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			nodeList = (NodeList) xPath.compile(expression).evaluate(umlDocument, XPathConstants.NODESET);
			return nodeList;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

	// public String changeStringXpath(String input, String attribute, String
	// changeString)
	// throws SAXException, IOException, ParserConfigurationException {
	// System.out.println(input);
	// InputSource source = new InputSource(new StringReader(input));

	// DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	// DocumentBuilder builder = builderFactory.newDocumentBuilder();
	// Document document = builder.parse(source);

	// XPathFactory xpathFactory = XPathFactory.newInstance();
	// XPath xpath = xpathFactory.newXPath();

	// String msg;
	// try {
	// msg = xpath.evaluate("/resp/msg", document);
	// } catch (XPathExpressionException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	public Document createCopy(Document originalDocument) throws ParserConfigurationException {
		Node originalRoot = originalDocument.getDocumentElement();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document copiedDocument = builder.newDocument();
		Node copiedRoot = copiedDocument.importNode(originalRoot, true);
		copiedDocument.appendChild(copiedRoot);

		return copiedDocument;
	}

	public void findNodeAndChange(Document umlCompleteMutant, String idUmlNode, String qgToChange) {
		NodeList allGatesNode = evaluateExpresion(umlCompleteMutant, "//node");
		for (int i = 0; i < allGatesNode.getLength(); i++) {
			NamedNodeMap attrGate = allGatesNode.item(i).getAttributes();
			if (attrGate.getNamedItem("xmi:id").getTextContent().equals(idUmlNode)) {
				Element value = (Element) allGatesNode.item(i);
				System.out.println(qgToChange);
				value.setAttribute("name", qgToChange);
				TransformerFactory factory = TransformerFactory.newInstance();
				try {
					factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
					Transformer xformer;
					xformer = factory.newTransformer();
					xformer.setOutputProperty(OutputKeys.INDENT, "yes");
					Writer output = new StringWriter();
					xformer.transform(new DOMSource(umlCompleteMutant), new StreamResult(output));

				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void saveFile(Document uml) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			FileWriter writer = new FileWriter(
					new File("umlModels\\mutantsQG\\mutant" + mutantCounter + ".uml"));
			++mutantCounter;
			DOMSource source = new DOMSource(uml);
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);

		} catch ( IOException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}