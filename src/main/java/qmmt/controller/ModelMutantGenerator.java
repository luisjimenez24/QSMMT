package qmmt.controller;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import qmmt.model.QuantumGatesEnum;
import qmmt.model.UmlEnum;
import qmmt.utils.DefaultParser;

public class ModelMutantGenerator {
    static DefaultParser dp;
    String route = "umlModels\\prueba.uml";

    public static void initializeModelMutantGenerator(String route){
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
		ArrayList<Document> mutantsQGI = new ArrayList<>();
		ArrayList<Document> mutantsQMI = new ArrayList<>();
		ArrayList<Document> mutantsQMD = new ArrayList<>();

		// Se itera sobre el número de puertas cuánticas del diagrama
		for (Node n : qGates) {
			NamedNodeMap attributes = n.getAttributes();
			String qgName = attributes.getNamedItem("name").getTextContent();
			if(qgName.toUpperCase().equals("M")){
					mutantsQMD.add(createMutantQGD(uml, n));
				}
			for (QuantumGatesEnum qgs : QuantumGatesEnum.values()) {
				if (qgName.toUpperCase().equals(qgs.getQuantumGate())) {
					mutantsQGR.addAll(createMutantQGR(uml, qgs, n));
					mutantsQGD.add(createMutantQGD(uml, n));
					mutantsQGI.addAll(createMutantQGI(uml, qgs, n));
					mutantsQMI.add(mutantcreateMutantQMI(uml, n));
				}
			}
		}
		saveMutants(mutantsQGR, "umlModels\\mutantsQGR\\mutantQGR");
		saveMutants(mutantsQGD, "umlModels\\mutantsQGD\\mutantQGD");
		saveMutants(mutantsQGI, "umlModels\\mutantsQGI\\mutantQGI");
		saveMutants(mutantsQMI, "umlModels\\mutantsQMI\\mutantQMI");
		saveMutants(mutantsQMD, "umlModels\\mutantsQMD\\mutantQMD");
	}

	private static void saveMutants(ArrayList<Document> mutants, String pathToSave){
		for (int i = 0; i < mutants.size(); i++) {
			changeNamePackagedElement(mutants.get(i), pathToSave.substring(pathToSave.length()-9) + i );
			String path = pathToSave + i + ".uml";
			dp.saveFile(mutants.get(i), path);
		}
	}

	//Método para cambiar el Activity Model para generar varios Qiskit Programs
	private static void changeNamePackagedElement(Document document, String newName) {
		String evPackagedElementName = "//packagedElement";
		NodeList packagedNodeList = dp.evaluateExpresion(document, evPackagedElementName);
		Node packagedElement = packagedNodeList.item(0);
		packagedElement.getAttributes().getNamedItem("name").setNodeValue(newName);
	}

	private static Document mutantcreateMutantQMI(Document uml, Node umlNode) {
		Document umlMutant = null;
			try {
				umlMutant = dp.createCopy(uml);

				// Cambiamos la siguiente puerta y obtenemos su ID
				String idNextQg = changeNextQgQGI(umlMutant, umlNode);

				// Cambiamos edge previo
				String idPrevEdge = changePreviousEdgeQGI(umlMutant, umlNode);

				// Insertamos el nuevo edge
				insertNewEdgeQGI(umlMutant, idNextQg);

				// Insertamos la nueva QG
				insertNewQG(umlMutant, idPrevEdge, "M", "uml:CallOperationAction");

			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return umlMutant;
	}

	// Método para crear los mutantes con el método de Quantum Gate Insertion
	private static ArrayList<Document> createMutantQGI(Document umlComplete, QuantumGatesEnum quantumGateFound,
			Node umlNode) {
		ArrayList<Document> mutants = new ArrayList<>();

		// Conseguimos las equivalencias de las puertas cuanticas para insertarlas
		// posteriormente
		for (int i = 0; i < quantumGateFound.getEquivalences().length; i++) {
			Document umlMutant;
			try {
				umlMutant = dp.createCopy(umlComplete);

				// Cambiamos la siguiente puerta y obtenemos su ID
				String idNextQg = changeNextQgQGI(umlMutant, umlNode);

				// Cambiamos edge previo
				String idPrevEdge = changePreviousEdgeQGI(umlMutant, umlNode);

				// Insertamos el nuevo edge
				insertNewEdgeQGI(umlMutant, idNextQg);

				// Insertamos la nueva QG
				insertNewQG(umlMutant, idPrevEdge, quantumGateFound.getEquivalences()[i], "uml:CallOperationAction");

				mutants.add(umlMutant);

			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return mutants;
	}

	// Creamos el nod que va a introducirse
	private static void insertNewQG(Document umlMutant, String idPrevEdge, String quantumGateEq, String umlAction) {
		// Conseguimos el anterior edge para asi conseguir el nodo de la anterior
		// puerta.
		String evGetPrevEdge = "//edge[@id=\"" + idPrevEdge + "\"]";
		NodeList previousEdgeNodeList = dp.evaluateExpresion(umlMutant, evGetPrevEdge);

		String idNextQg = previousEdgeNodeList.item(0).getAttributes().getNamedItem("xmi:id").getTextContent();
		String evGetPreviousQG = "//node[@outgoing=\"" + idNextQg + "\"]";
		NodeList previousQg = dp.evaluateExpresion(umlMutant, evGetPreviousQG);

		// Conseguimos el valor del atributo "inPartition" de la anterior puerta
		Element newEdge = umlMutant.createElement("node");
		newEdge.setAttribute("inPartition",
				previousQg.item(0).getAttributes().getNamedItem("inPartition").getTextContent());
		newEdge.setAttribute("outgoing", "edgeMutant");
		newEdge.setAttribute("incoming", idPrevEdge);
		newEdge.setAttribute("xmi:id", "idMutant");
		newEdge.setAttribute("name", quantumGateEq);
		newEdge.setAttribute("xmi:type", umlAction);

		// Creamos el elemento <QuantumUMLProfile:QuantumGate> para indicar que se crea una nueva puerta cuántica con el QUML Profile
		Element qUmlProfilElement = umlMutant.createElement("QuantumUMLProfile:QuantumGate");
		qUmlProfilElement.setAttribute("xmi:id", "qUmlProfile");
		qUmlProfilElement.setAttribute("base_Action", "idMutant");

		//Añadimos los dos elementos que hemos creado al umlMutant
		umlMutant.getElementsByTagName("node").item(0).getParentNode().appendChild(newEdge);
		umlMutant.getElementsByTagName("QuantumUMLProfile:QuantumGate").item(0).getParentNode().appendChild(qUmlProfilElement);

	}

	// Creamos el edge que va a conectar la nueva puerta con la siguiente puerta
	private static void insertNewEdgeQGI(Document umlMutant, String idNextQg) {
		Element newEdge = umlMutant.createElement("edge");
		newEdge.setAttribute("xmi:id", "edgeMutant");
		newEdge.setAttribute("xmi:type", "uml:ControlFlow");
		newEdge.setAttribute("target", idNextQg);
		newEdge.setAttribute("source", "idMutant");
		umlMutant.getElementsByTagName("edge").item(0).getParentNode().appendChild(newEdge);

	}

	// Cambiamos el attr "incoming" de la siguiente puerta cuántica y obtenemos su
	// ID
	private static String changeNextQgQGI(Document umlMutant, Node umlNode) {
		// Conseguimos el siguiente edge
		String outgoingEdge = umlNode.getAttributes().getNamedItem("outgoing").getTextContent();
		String evfindEdge = "//edge[@id=\"" + outgoingEdge + "\"]";
		NodeList previousEdgeNodelist = dp.evaluateExpresion(umlMutant, evfindEdge);
		Node previousNode = previousEdgeNodelist.item(0);

		// Conseguimos la siguiente puerta cuántica
		String idTargetEdge = previousNode.getAttributes().getNamedItem("target").getTextContent();
		String evfindNode = "//node[@id=\"" + idTargetEdge + "\"]";
		NodeList nextQgNodelist = dp.evaluateExpresion(umlMutant, evfindNode);
		Node nextQg = nextQgNodelist.item(0);

		// cambiamos el id de la siguiente puerta cuántica
		nextQg.getAttributes().getNamedItem("incoming").setNodeValue("edgeMutant");

		return nextQg.getAttributes().getNamedItem("xmi:id").getTextContent();
	}

	// Cambiamos el edge que apunta de la puerta cuántica previa a la nueva
	private static String changePreviousEdgeQGI(Document umlMutant, Node umlNode) {
		String outgoingEdge = umlNode.getAttributes().getNamedItem("outgoing").getTextContent();
		String evfindEdge = "//edge[@id=\"" + outgoingEdge + "\"]";

		NodeList previousEdgeNodelist = dp.evaluateExpresion(umlMutant, evfindEdge);
		Node previousNode = previousEdgeNodelist.item(0);
		previousNode.getAttributes().getNamedItem("target").setNodeValue("idMutant");

		return previousNode.getAttributes().getNamedItem("xmi:id").getTextContent();
	}

	// Metodo para hacer los mutantes mediante Quantum Gate Deletion
	private static Document createMutantQGD(Document uml, Node qgNode) {
		Document umlMutant = null;
		try {
			umlMutant = dp.createCopy(uml);
		} catch (ParserConfigurationException e) {
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

	// Se borra el id de la puerta cuántica del atributo "node" del qubit (indica
	// las puertas cuánticas sobre las que se aplica)
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

	// Se bora el nodo de la puerta cuántica para la mutación QGD
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

	// Se obtiene la siguiente puerta cuántica
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

	// Creamos los mutantes mediante Quantum Gate Replacement
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
