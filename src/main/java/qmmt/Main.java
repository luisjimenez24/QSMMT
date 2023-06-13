package qmmt;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import qmmt.model.QuantumGatesEnum;
import qmmt.model.UmlEnum;
import qmmt.utils.DefaultParser;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {
	static DefaultParser dp;

	public static void main(String[] args) {

		String route = "../QModellingMutationTesting/umlModels/prueba.uml";
		File f = new File(route);
		dp = new DefaultParser(f);
		Document uml = dp.buildDocument();
		ArrayList<Document> umlQGR = qGateReplacement(uml);
	}

	private static ArrayList<Document> qGateReplacement(Document uml) {
		ArrayList<Node> qGates = getGatesNodes(uml);
		ArrayList<Document> mutants = new ArrayList<>();

		for (Node n : qGates) {
			NamedNodeMap attributes = n.getAttributes();
			String qgName = attributes.getNamedItem("name").getTextContent();
			for (QuantumGatesEnum qgs : QuantumGatesEnum.values()) {
				if (qgName.toUpperCase().equals(qgs.getQuantumGate())) {
					mutants = createMutantQGR(uml, qgName, qgs, n);
					System.out.println("Puerta cuantica encontrada: " + qgName + " es el enum " + qgs.name());
				}
			}
		}

		return null;

	}

	private static ArrayList<Document> createMutantQGR(Document uml, String qgName, QuantumGatesEnum qgs, Node umlNode) {
		ArrayList<Document> mutants = new ArrayList<>();
		String idOriginalUml = 	umlNode.getAttributes().getNamedItem("xmi:id").getTextContent();

		for(int i = 0; i<qgs.getEquivalences().length;i++){
			Document mutant = dp.createCopy(uml);
			Node n = umlNode.cloneNode(false);
			n.getAttributes().getNamedItem("name").setNodeValue(qgs.getEquivalences()[i]);
			Node mutantNode = seekNode(mutant, n.getAttributes().getNamedItem("xmi:id"));
			// Node attributeNode = n.getAttributes().getNamedItem("name");
			// attributeNode.setNodeValue(qgs.getEquivalences()[i]);
			// mutant.replaceChild(attributeNode, umlNode);
			// mutants.add(mutant);
		}

		return mutants;
	}

	private static Node seekNode(Document mutant, Node originalId) {
		NodeList allNodes = mutant.getChildNodes();

		for(int i = 0; i<allNodes.getLength();i++){
			NamedNodeMap n = allNodes.item(i).getAttributes();



				// if(n.equals(originalId)){

				// }
				}

		return null;
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

	private ArrayList<Document> mutSingleUnitary(Document uml) {

		return null;
	}
}
