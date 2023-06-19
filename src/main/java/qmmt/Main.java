package qmmt;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import qmmt.model.QuantumGatesEnum;
import qmmt.model.UmlEnum;
import qmmt.utils.DefaultParser;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Main {
	static DefaultParser dp;

	public static void main(String[] args) {
		String route = "umlModels\\prueba.uml";
		File f = new File(route);
		dp = new DefaultParser(f);
		Document uml = dp.buildDocument();
		createMutants(uml);
	}


	private static void createMutants(Document uml) {
		ArrayList<Node> qGates = getGatesNodes(uml);
		ArrayList<Document> mutantsQGR = new ArrayList<>();
		ArrayList<Document> mutantsQGD = new ArrayList<>();
		
		for (Node n : qGates) {
			NamedNodeMap attributes = n.getAttributes();
			String qgName = attributes.getNamedItem("name").getTextContent();
			for (QuantumGatesEnum qgs : QuantumGatesEnum.values()) {
				if (qgName.toUpperCase().equals(qgs.getQuantumGate())) {
					//mutantsQGR.addAll(createMutantQGR(uml, qgs, n));
					mutantsQGD.add(createMutantGGD(uml, n));
				}
			}
		}
		for (Document d : mutantsQGR){
			dp.saveFile(d);
		}
	}

	private static Document createMutantGGD(Document uml, Node qgNode) {
		Document umlMutant = null;
		try {
			umlMutant = dp.createCopy(uml);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Consigo el id del edge que apunta a la puerta cuántica actual
		String qgId = qgNode.getAttributes().getNamedItem("xmi:id").getTextContent();
		String evIncomingEdge ="//edge[@target=\""+qgId+"\"]";
		NodeList edgeIncoming = dp.evaluateExpresion(umlMutant, evIncomingEdge);

		//Una vez conseguido el id del nodo previo, conseguimos su Node
		String idPreviousNode = edgeIncoming.item(0).getAttributes().getNamedItem("source").getTextContent();
		String evPreviousNode ="//node[@id=\""+idPreviousNode+"\"]";
		NodeList previousNode = dp.evaluateExpresion(umlMutant, evPreviousNode);

		//Primer delete: el elemento de <QuantumUMLProfile:QuantumGate...
		dp.deleteBaseAction(umlMutant, qgId); 

		try {
			printDocument(umlMutant, System.out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*Ahora se pueden dar 3 posibles casos: 
			- 1º El nodo de la puerta cuántica sea el primero del UML -> El nodo previo es un Initial Node
			- 2º El nodo de la puerta cuántica es el último del UML -> El nodo posterior es un Final Node
			- 3º El nodo de la puerta cuántica es intermedio -> 💀

		Evaluamos si nos encontramos en el primer o tercer caso
		*/

		String previousNodeType = previousNode.item(0).getAttributes().getNamedItem("xmi:type").getTextContent();

		if(previousNodeType.equals("uml:InitialNode")){
			System.out.println("La puerta "+qgId+" tiene antes un Initial Node");
		}else {
			
		}
		return null;
	}


	private static ArrayList<Document> createMutantQGR(Document umlComplete, QuantumGatesEnum quantumGateFound,
			Node umlNode) {
		ArrayList<Document> mutants = new ArrayList<>();

		NamedNodeMap umlNodeAttr = umlNode.getAttributes();
		String idUmlNode = umlNodeAttr.getNamedItem("xmi:id").getTextContent();
		System.out.println("Trabajando con el nodo: " + idUmlNode);

		for (int i = 0; i < quantumGateFound.getEquivalences().length; i++) {
			Document umlCompleteMutant;
			try {
				umlCompleteMutant = dp.createCopy(umlComplete);
				dp.findNodeAndChange(umlCompleteMutant, idUmlNode, quantumGateFound.getEquivalences()[i]);
				mutants.add(umlCompleteMutant);

			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mutants;
	}

	private static ArrayList<Node> getGatesNodes(Document umlDocument) {
		String nodes = "//node";
		ArrayList<Node> qGatesNodes = new ArrayList<>();
		NodeList qGatesUml = dp.evaluateExpresion(umlDocument, nodes);

		for (int i = 0; i < qGatesUml.getLength(); i++) {
			NamedNodeMap attributes = qGatesUml.item(i).getAttributes();
			if (attributes.getNamedItem("xmi:type").getTextContent()
					.equals(UmlEnum.QUANTUM_GATE.getType())) {
				qGatesNodes.add(qGatesUml.item(i));
			}
		}
		return qGatesNodes;
	}

	public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc),
				new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}
}
