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
import java.io.IOException;

import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;

public class Main {
	static DefaultParser dp;

	public static void main(String[] args) {
		String route = "umlModels\\prueba.uml";
		File f = new File(route);
		dp = new DefaultParser(f);
		Document uml = dp.buildDocument();
		createMutants(uml);
	}

	// Metodo para crear los mutantes mediante las distintas formas
	private static void createMutants(Document uml) {
		ArrayList<Node> qGates = getGatesNodes(uml);
		ArrayList<Document> mutantsQGR = new ArrayList<>();
		ArrayList<Document> mutantsQGD = new ArrayList<>();

		// Se itera sobre el número de puertas cuánticas del diagrama
		for (Node n : qGates) {
			NamedNodeMap attributes = n.getAttributes();
			String qgName = attributes.getNamedItem("name").getTextContent();
			for (QuantumGatesEnum qgs : QuantumGatesEnum.values()) {
				if (qgName.toUpperCase().equals(qgs.getQuantumGate())) {
					mutantsQGR.addAll(createMutantQGR(uml, qgs, n));
					mutantsQGD.add(createMutantGGD(uml, n));
				}
			}
		}
		for (int i = 0; i < mutantsQGR.size(); i++) {
			String pathToSave = "umlModels\\mutantsQGR\\mutant" + i + ".uml";
			dp.saveFile(mutantsQGR.get(i), pathToSave);
		}
		for (int i = 0; i < mutantsQGD.size(); i++) {
			String pathToSave = "umlModels\\mutantsQGD\\mutant" + i + ".uml";
			dp.saveFile(mutantsQGD.get(i), pathToSave);
		}
	}

	// Metodo para hacer los mutantes mediante Quantum Gate Deletion
	private static Document createMutantGGD(Document uml, Node qgNode) {
		Document umlMutant = null;
		try {
			umlMutant = dp.createCopy(uml);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Conseguimos el id de la puerta con la que estamos trabajando
		String qgId = qgNode.getAttributes().getNamedItem("xmi:id").getTextContent();

		// Conseguimos los nodos del UML que corresponde a la QG previa y la siguiente,
		// respectivamente
		NodeList nextQg = getNextQg(umlMutant, qgId);

		// Primer delete: el elemento de <QuantumUMLProfile:QuantumGate...
		deleteBaseAction(umlMutant, qgId);

		// Segundo delete: en el atributo "node" del packagedElement aparece el ID de la
		// puerta cuántica a borrar
		deletePackagedElementNodeAttribute(umlMutant, qgId);

		// Tercer delete: en el atributo "node" del qubit donde se aplica aparece el ID
		// de la puerta cuántica a borrar
		deleteQubitNodeAttr(umlMutant, qgId);

		// Cuarto delete: el nodo de la puerta cuántica
		deleteQuantumGateNode(umlMutant, qgId);

		// Quinto delete: el edge que apunta de la puerta cuántica que estamos borrando
		// con la siguiente
		deleteNextEdge(umlMutant, qgId);

		// Modificación del previous edge
		String idNextQg = nextQg.item(0).getAttributes().getNamedItem("xmi:id").getTextContent();
		String idModifiedEdge = modifyPreviousEdge(umlMutant, qgId, idNextQg);

		// Modificación del atributo "incoming" de la siguiente puerta
		modifyNextQuantumGateNode(umlMutant, idNextQg, idModifiedEdge);

		// Modificacion del atributo "edge" del qubit en caso de que no exista
		modifyEdgeQubitAttr(umlMutant, idModifiedEdge);

		return umlMutant;
	}

	// Con este método se borra el nodo " <QuantumUMLProfile:QuantumGate>" que hace
	// referencia a la puerta que se va a borrar
	public static void deleteBaseAction(Document umlMutant, String qgId) {
		Element root = umlMutant.getDocumentElement();
		Node deleteNode = null;
		for (int i = 0; i < root.getChildNodes().getLength(); i++) {
			if (root.getChildNodes().item(i).getNodeName().equals("QuantumUMLProfile:QuantumGate")) {
				NamedNodeMap att = root.getChildNodes().item(i).getAttributes();
				if (att.getNamedItem("base_Action").getTextContent().equals(qgId)) {
					deleteNode = root.getChildNodes().item(i);
				}
			}
		}

		deleteNode.getParentNode().removeChild(deleteNode);
	}

	// Se bora el id de la puerta cuantica a borrar del atributo "node" del
	// "<packagedElement>"
	private static void deletePackagedElementNodeAttribute(Document umlMutant, String qgId) {
		NodeList packagedElemNodeList = dp.evaluateExpresion(umlMutant, "//packagedElement");
		Node packagedElem = packagedElemNodeList.item(0);
		Node attrPackagedElm = packagedElem.getAttributes().getNamedItem("node");

		String[] attr = attrPackagedElm.getTextContent().split(" ");
		String setNodeAttr = "";
		for (String s : attr) {
			if (!s.equals(qgId)) {
				setNodeAttr += s + " ";
			}
		}
		attrPackagedElm.setNodeValue(setNodeAttr);
	}

	//Se borra el id de la puerta cuántica del atributo "node" del qubit (indica las puertas cuánticas sobre las que se aplica)
	private static void deleteQubitNodeAttr(Document umlMutant, String qgId) {
		NodeList qubits = dp.evaluateExpresion(umlMutant, "//group");

		for (int i = 0; i < qubits.getLength(); i++) {
			Node qubit = qubits.item(i);
			Node qubitNodeAttr = qubit.getAttributes().getNamedItem("node");

			if (qubitNodeAttr.getTextContent().contains(qgId)) {
				String[] attr = qubitNodeAttr.getTextContent().split(" ");
				String setNodeAttr = "";
				for (String s : attr) {
					if (!s.equals(qgId)) {
						setNodeAttr += s + " ";
					}
				}
				qubitNodeAttr.setNodeValue(setNodeAttr);
				i = qubits.getLength();
			}
		}
	}

	//Se bora el nodo de la puerta cuántica para la mutación QGD
	private static void deleteQuantumGateNode(Document umlMutant, String qgId) {
		NodeList quantumGates = dp.evaluateExpresion(umlMutant, "//node");
		Node qgToDelete = null;
		for (int i = 0; i < quantumGates.getLength(); i++) {
			Node quantumGate = quantumGates.item(i);
			Node qubitNodeAttr = quantumGate.getAttributes().getNamedItem("xmi:id");
			if (qubitNodeAttr.getTextContent().equals(qgId)) {
				qgToDelete = quantumGates.item(i);
			}
		}
		qgToDelete.getParentNode().removeChild(qgToDelete);
	}

	private static void modifyEdgeQubitAttr(Document umlMutant, String nextEdge) {
		String evGetQubits = "//group";
		NodeList qubits = dp.evaluateExpresion(umlMutant, evGetQubits);

		// Buscamos si en algún id
		for (int i = 0; i < qubits.getLength(); i++) {
			String idEdge = qubits.item(i).getAttributes().getNamedItem("edge").getTextContent();
			String evDeletedIdEdge = "//*[@id=\"" + idEdge + "\"]";
			NodeList elementsWithThisId = dp.evaluateExpresion(umlMutant, evDeletedIdEdge);

			if (elementsWithThisId.getLength() == 0) {
				Node qubitToReplace = qubits.item(0);
				qubitToReplace.getAttributes().getNamedItem("edge").setNodeValue(nextEdge);
			}
		}
	}

	// Las puertas cuánticas tienen el atributo "incoming" que apuntan el edge
	// previo. Hay que cambiarlo por el nuevo edge que va a apuntar a la QG
	private static void modifyNextQuantumGateNode(Document umlMutant, String idNextQg, String idModifiedEdge) {
		String evModifiedEdge = "//edge[@id=\"" + idModifiedEdge + "\"]";
		NodeList modifiedEdgeList = dp.evaluateExpresion(umlMutant, evModifiedEdge);
		Node modifiedEdgNode = modifiedEdgeList.item(0);

		String evNextQuantumGate = "//node[@id=\"" + idNextQg + "\"]";
		NodeList nextQGNodelist = dp.evaluateExpresion(umlMutant, evNextQuantumGate);

		Node quantumGate = nextQGNodelist.item(0);
		quantumGate.getAttributes().getNamedItem("incoming")
				.setNodeValue(modifiedEdgNode.getAttributes().getNamedItem("xmi:id").getTextContent());

		// nextQuantumGate.getAttributes().getNamedItem("incoming").setNodeValue(idModifiedEdge);
	}

	// Con este método se pretende modificar el atributo "target" del edge que
	// apunta a la QG borrada por el id del siguiente nodo
	private static String modifyPreviousEdge(Document umlMutant, String qgId, String idNextQg) {
		// Conseguimos el Node del edge previo
		String evIncomingEdge = "//edge[@target=\"" + qgId + "\"]";
		NodeList edgeIncoming = dp.evaluateExpresion(umlMutant, evIncomingEdge);

		// Modificamos el atributo "target"
		Node incomingEdg = edgeIncoming.item(0);
		incomingEdg.getAttributes().getNamedItem("target").setNodeValue(idNextQg);

		return incomingEdg.getAttributes().getNamedItem("xmi:id").getTextContent();
	}

	// Borramos el ControlFlow que apunta a la siguiente puerta cuántica
	private static void deleteNextEdge(Document umlMutant, String qgId) {
		String evEdgeNextNode = "//edge[@source=\"" + qgId + "\"]";
		NodeList nextEdgeNode = dp.evaluateExpresion(umlMutant, evEdgeNextNode);
		Node nexEdge = nextEdgeNode.item(0);
		nexEdge.getParentNode().removeChild(nexEdge);
	}

	//Se obtiene la siguiente puerta cuántica
	private static NodeList getNextQg(Document umlMutant, String qgId) {
		// Conseguimos el edge que apunta a la siguiente puerta
		String evEdgeNextNode = "//edge[@source=\"" + qgId + "\"]";
		NodeList nextEdgeNode = dp.evaluateExpresion(umlMutant, evEdgeNextNode);

		// Conseguimos el edge que apunta a la siguiente puerta
		String idNextNode = nextEdgeNode.item(0).getAttributes().getNamedItem("target").getTextContent();
		String evNextNode = "//node[@id=\"" + idNextNode + "\"]";
		NodeList nextNode = dp.evaluateExpresion(umlMutant, evNextNode);

		return nextNode;
	}

	//Creamos los mutantes mediante Quantum Gate Replacement
	private static ArrayList<Document> createMutantQGR(Document umlComplete, QuantumGatesEnum quantumGateFound,
			Node umlNode) {
		ArrayList<Document> mutants = new ArrayList<>();

		NamedNodeMap umlNodeAttr = umlNode.getAttributes();
		String idUmlNode = umlNodeAttr.getNamedItem("xmi:id").getTextContent();

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

}
