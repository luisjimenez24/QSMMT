package qmmt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DefaultParser {

	private File file;
	DocumentBuilderFactory builderFactory;
	DocumentBuilder builder;

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
			builderFactory = DocumentBuilderFactory.newInstance();
			builder = builderFactory.newDocumentBuilder();
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
	public Document createCopy(Document originalDocument){
        Node originalRoot = originalDocument.getDocumentElement();
        Document copiedDocument = builder.newDocument();
        Node copiedRoot = copiedDocument.importNode(originalRoot, true);
        copiedDocument.appendChild(copiedRoot);

		return copiedDocument;
	}

}