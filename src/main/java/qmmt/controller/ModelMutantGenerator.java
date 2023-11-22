package qmmt.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.epsilon.egl.parse.Egx_EolParserRules.returnStatement_return;
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

	public static void initializeModelMutantGenerator(String route) {
		File f = new File(route);
		dp = new DefaultParser(f);
		Document uml = dp.buildDocument();
		createMutants(uml);
	}

	// Metodo para crear los mutantes mediante las distintas formas
	private static void createMutants(Document uml) {
		ArrayList<Node> oneQubitGates = getOneQubitGatesNodes(uml);
		HashMap<Node, Node> twoQubitgates = getTwoQubitGatesNodes(uml);

		ArrayList<Document> mutantsQGR = new ArrayList<>();
		ArrayList<Document> mutantsQGD = new ArrayList<>();
		ArrayList<Document> mutantsQGI = new ArrayList<>();
		ArrayList<Document> mutantsQMI = new ArrayList<>();
		ArrayList<Document> mutantsQMD = new ArrayList<>();

		// Se itera sobre el número de puertas cuánticas de un unico qubit del diagrama
		for (Node n : oneQubitGates) {
			NamedNodeMap attributes = n.getAttributes();
			String qgName = attributes.getNamedItem("name").getTextContent();
			if (qgName.toUpperCase().equals("M")) {
				// mutantsQMD.add(createMutantQGD(uml, n));
			}
			for (QuantumGatesEnum qgs : QuantumGatesEnum.values()) {
				if (qgName.toUpperCase().equals(qgs.getQuantumGate())) {
					// mutantsQGR.addAll(createMutantQGR(uml, qgs, n));
					// mutantsQGD.add(createMutantQGD(uml, n));
					// mutantsQGI.addAll(createMutantQGI(uml, qgs, n));
					// mutantsQMI.add(mutantcreateMutantQMI(uml, n));
				}
			}
		}

		// Se itera para realizar las operaciones cuánticas de dos qubits
		for (Map.Entry<Node, Node> entry : twoQubitgates.entrySet()) {
			for (QuantumGatesEnum qgs : QuantumGatesEnum.values()) {
				if ((entry.getKey().getAttributes().getNamedItem("name").getTextContent()
						+ entry.getValue().getAttributes().getNamedItem("name").getTextContent())
						.equals(qgs.getQuantumGate())) {

					// createTwoQubitsMutantQGR(uml, entry, qgs);
					// mutantsQGR.addAll(createTwoQubitsMutantQGR(uml, entry, qgs));
					 mutantsQGI.addAll(createTwoQubitsMutantQGI(uml, entry, qgs));
				}
			}
		}
		// saveMutants(mutantsQGR, "umlModels\\mutantsQGR\\mutantQGR");
		// saveMutants(mutantsQGD, "umlModels\\mutantsQGD\\mutantQGD");
		saveMutants(mutantsQGI, "umlModels\\mutantsQGI\\mutantQGI");
		// saveMutants(mutantsQMI, "umlModels\\mutantsQMI\\mutantQMI");
		// saveMutants(mutantsQMD, "umlModels\\mutantsQMD\\mutantQMD");
	}

	//Método para crear los mutantes uml de puertas cuánticas de dos qubits con el approach de QGI
	private static ArrayList<Document>createTwoQubitsMutantQGI(Document umlComplete, Entry<Node, Node> entry,
			QuantumGatesEnum quantumGateFound) {
		ArrayList<Document> mutants = new ArrayList<>();

		// Conseguimos las equivalencias de las puertas cuanticas para insertarlas
		// posteriormente
		for (int i = 0; i < quantumGateFound.getEquivalences().length; i++) {
			Document umlMutant;
			try {
				umlMutant = dp.createCopy(umlComplete);

				// Cambiamos la siguiente puerta y obtenemos su ID
				Node acceptEventActionNode = getAcceptEventActionNode(entry);
				String idNextQg = changeNextQgQGI(umlMutant, acceptEventActionNode);

				// Cambiamos edge previo
				//Node sendSignalActionNode = getSendSignalActionNode(entry);
				String idPrevEdge = changePreviousEdgeQGI(umlMutant, acceptEventActionNode);
				System.out.println(idPrevEdge);

				// Insertamos el nuevo edge
				insertNewEdgeQGI(umlMutant, idNextQg, "idMutant2");

				// Insertamos la nueva QG
				insertNewQG2Qubits(umlMutant, idPrevEdge, quantumGateFound.getEquivalences()[i], entry);

				// Añadimos la ownedRule de los constrainedElement
				insertNewConstrainedElement(umlMutant, entry, quantumGateFound.getEquivalences()[i]);

				// Insertamos los nuevos ids al Activity
				insertNewQGToActivity(umlMutant);

				// Insertamos los nuevos ids a los atributos 'node' de los qubits
				insertNewIdToQubits(umlMutant, entry);
				
				mutants.add(umlMutant);

			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mutants;
	}

	//Metodo para meter los nuevos ids a los correspondientes attributos 'node' de los qubits
	private static void insertNewIdToQubits(Document umlMutant, Entry<Node, Node> entry) {
		//Conseguimos los nodos para el nombre del constraint
		Node sendSignalActionNode = getSendSignalActionNode(entry);
		Node acceptEventActionNode = getAcceptEventActionNode(entry);

		String idQubit1 = sendSignalActionNode.getAttributes().getNamedItem("inPartition").getTextContent();
		String idQubit2 = acceptEventActionNode.getAttributes().getNamedItem("inPartition").getTextContent();
		String evExprQubit1 = "//group[@id=\"" + idQubit1 + "\"]";
		String evExprQubit2 = "//group[@id=\"" + idQubit2 + "\"]";
		
		NodeList nodeQubit1 = dp.evaluateExpresion(umlMutant, evExprQubit1);
		NodeList nodeQubit2 = dp.evaluateExpresion(umlMutant, evExprQubit2);

		//Metemos los ids correspondientes a cada qubit
		String newNodeAtrQ1 = nodeQubit1.item(0).getAttributes().getNamedItem("node").getTextContent() + " idMutant";
		String newNodeAtrQ2 = nodeQubit2.item(0).getAttributes().getNamedItem("node").getTextContent() + " idMutant2";
		nodeQubit1.item(0).getAttributes().getNamedItem("node").setNodeValue(newNodeAtrQ1);
		nodeQubit2.item(0).getAttributes().getNamedItem("node").setNodeValue(newNodeAtrQ2);
	}

	//Cambiamos el atributo 'node' del packagedElement introduciendo los nuevos ids
	private static void insertNewQGToActivity(Document umlMutant) {
		String evPackagedElementName = "//packagedElement";
		NodeList packagedNodeList = dp.evaluateExpresion(umlMutant, evPackagedElementName);

		String newNodeAttr = packagedNodeList.item(0).getAttributes().getNamedItem("node").getTextContent() + " idMutant idMutant2";
		packagedNodeList.item(0).getAttributes().getNamedItem("node").setNodeValue(newNodeAttr);		
	}

	//Metodo para introducir el ConstrainedElement que implica la introducción de la nueva gate
	private static void insertNewConstrainedElement(Document umlMutant, Entry<Node, Node> entry, String newQg) {
		//Conseguimos los nodos para el nombre del constraint
		Node sendSignalActionNode = getSendSignalActionNode(entry);
		Node acceptEventActionNode = getAcceptEventActionNode(entry);

		String idQubit1 = sendSignalActionNode.getAttributes().getNamedItem("inPartition").getTextContent();
		String idQubit2 = acceptEventActionNode.getAttributes().getNamedItem("inPartition").getTextContent();
		String evExprQubit1 = "//group[@id=\"" + idQubit1 + "\"]";
		String evExprQubit2 = "//group[@id=\"" + idQubit2 + "\"]";
		
		NodeList nodeQubit1 = dp.evaluateExpresion(umlMutant, evExprQubit1);
		NodeList nodeQubit2 = dp.evaluateExpresion(umlMutant, evExprQubit2);

		// Creamos el node del ownedRule
		Element newNodeSSA = umlMutant.createElement("ownedRule");
		newNodeSSA.setAttribute("constrainedElement", "idMutant idMutant2");
		newNodeSSA.setAttribute("xmi:id", "newConstraint");
		newNodeSSA.setAttribute("name", newQg + "("+nodeQubit1.item(0).getAttributes().getNamedItem("name").getTextContent()+
		"-"+nodeQubit2.item(0).getAttributes().getNamedItem("name").getTextContent()+")");
		newNodeSSA.setAttribute("xmi:type", "uml:Constraint");
		
		//Creamos el node del specification
		Element newSpecification = umlMutant.createElement("specification");
		newSpecification.setAttribute("name", "constrainedSpecMutant");
		newSpecification.setAttribute("xmi:type", "uml:OpaqueExpression");
		newSpecification.setAttribute("xmi:id", "newSpec");

		//Creamos el nodo del language
		Element newLanguage = umlMutant.createElement("language");
		newLanguage.setTextContent("Natural language");

		//Creamos el nodo del language
		Element newBody = umlMutant.createElement("body");
		newBody.setTextContent(newQg);

		// Una vez creados los nodos los añadimos al UML
		newSpecification.appendChild(newLanguage);
		newSpecification.appendChild(newBody);
		newNodeSSA.appendChild(newSpecification);
		umlMutant.getElementsByTagName("edge").item(0).getParentNode().appendChild(newNodeSSA);
	}

	//Método para insertar los dos nodos de la nueva puerta cuántica de dos qubits
	private static void insertNewQG2Qubits(Document umlMutant, String idPrevEdge, String newQg, Entry<Node, Node> entry) {
		Node acceptEventActionNode = getAcceptEventActionNode(entry);
		Node sendSignalActionNode = getSendSignalActionNode(entry);
		String[] divisionQG = findDivisionQGReplace(newQg);

		// Creamos el node del SendSignalAction
		Element newNodeSSA = umlMutant.createElement("node");
		newNodeSSA.setAttribute("inPartition",
				sendSignalActionNode.getAttributes().getNamedItem("inPartition").getTextContent());
		newNodeSSA.setAttribute("incoming", idPrevEdge);
		newNodeSSA.setAttribute("xmi:id", "idMutant");
		newNodeSSA.setAttribute("name", divisionQG[0].toUpperCase());
		newNodeSSA.setAttribute("xmi:type", "uml:SendSignalAction");
		
		// Creamos el node del AcceptEventAction
		Element newNodeAEA = umlMutant.createElement("node");
		newNodeAEA.setAttribute("inPartition",
				acceptEventActionNode.getAttributes().getNamedItem("inPartition").getTextContent());
		newNodeAEA.setAttribute("outgoing", "edgeMutant");
		newNodeAEA.setAttribute("xmi:id", "idMutant2");
		newNodeAEA.setAttribute("name", divisionQG[1].toUpperCase());
		newNodeAEA.setAttribute("xmi:type", "uml:AcceptEventAction");

		// Creamos los dos elements <QuantumUMLProfile:QuantumGate> y <QuantumUMLProfile:ControlledQubit> para indicar que se crea
		// una nueva puerta cuántica de dos qubits con el QUML Profile 
		Element qUmlProfilElementSSA = umlMutant.createElement("QuantumUMLProfile:ControlledQubit");
		qUmlProfilElementSSA.setAttribute("xmi:id", "qUmlProfile");
		qUmlProfilElementSSA.setAttribute("base_SendSignalAction", "idMutant");

		Element qUmlProfilElementAEA = umlMutant.createElement("QuantumUMLProfile:QuantumGate");
		qUmlProfilElementAEA.setAttribute("xmi:id", "qUmlProfile");
		qUmlProfilElementAEA.setAttribute("base_AcceptEventAction", "idMutant2");
		qUmlProfilElementAEA.setAttribute("base_Action", "idMutant2");

		// Añadimos los elementos que hemos creado al umlMutant
		umlMutant.getElementsByTagName("node").item(0).getParentNode().appendChild(newNodeSSA);
		umlMutant.getElementsByTagName("node").item(0).getParentNode().appendChild(newNodeAEA);

		umlMutant.getElementsByTagName("QuantumUMLProfile:QuantumGate").item(0).getParentNode()
				.appendChild(qUmlProfilElementSSA);
		umlMutant.getElementsByTagName("QuantumUMLProfile:QuantumGate").item(0).getParentNode()
				.appendChild(qUmlProfilElementAEA);
	}

	private static Node getSendSignalActionNode(Entry<Node, Node> entry) {
		if(entry.getKey().getAttributes().getNamedItem("xmi:type").getTextContent().equals("uml:SendSignalAction")){
			return entry.getKey();
		}else{
			return entry.getValue();
		}
	}

	//Metodo para conseguir el node de tipo AcceptEventAction de las puertas cuánticas de dos qubits
	private static Node getAcceptEventActionNode(Entry<Node, Node> entry) {
		if(entry.getKey().getAttributes().getNamedItem("xmi:type").getTextContent().equals("uml:AcceptEventAction")){
			return entry.getKey();
		}else{
			return entry.getValue();
		}
	}

	// Método para llevar a cabo el Quantum Gate Replacement a una puerta de 2
	// qubits
	private static ArrayList<Document> createTwoQubitsMutantQGR(Document uml, Entry<Node, Node> entry,
			QuantumGatesEnum qgs) {
		ArrayList<Document> mutants = new ArrayList<>();

		for (int i = 0; i < qgs.getEquivalences().length; i++) {
			Document umlMutant = null;
			try {
				umlMutant = dp.createCopy(uml);

				// Primero: cambiar el "name" en el "ownedRule"
				Node ownedRule = getOwnedRule(umlMutant,
						entry.getKey().getAttributes().getNamedItem("xmi:id").getTextContent() + " "
								+ entry.getValue().getAttributes().getNamedItem("xmi:id").getTextContent());
				changeOwnedRuleName(ownedRule, qgs.getEquivalences()[i]);

				// Segundo: cambiar el name del AcceptEventAction
				ArrayList<Node> nodesToChange = getAcceptAndSendSignNodes(umlMutant, entry);
				changeNodesNameToReplace(nodesToChange, qgs.getEquivalences()[i]);

				mutants.add(umlMutant);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println(mutants.size());
		return mutants;
	}

	//Método para cambiar el nombre de las puertas cuánticas de dos qubits usando el approach del QGR
	private static void changeNodesNameToReplace(ArrayList<Node> nodesToChange, String qGateReplace) {
		String[] divisionQG = findDivisionQGReplace(qGateReplace);
		for (Node n : nodesToChange) {
			if (n.getAttributes().getNamedItem("xmi:type").getTextContent().equals("uml:SendSignalAction")) {
				System.out.println(divisionQG[0]);
				n.getAttributes().getNamedItem("name").setNodeValue(
						divisionQG[0]);
			} else if (n.getAttributes().getNamedItem("xmi:type").getTextContent().equals("uml:AcceptEventAction")) {
				n.getAttributes().getNamedItem("name").setNodeValue(
						divisionQG[1]);
			}
		}
	}

	//Método para encontrar la division de la puerta cuántica
	private static String[] findDivisionQGReplace(String qGateReplace) {
		String[] qgDivision = null;
		for (QuantumGatesEnum qgs : QuantumGatesEnum.values()) {
			if (qGateReplace.toUpperCase().equals(qgs.getQuantumGate())) {
				String division = qgs.getDivision();
				qgDivision = division.split("-");
			}
		}
		return qgDivision;
	}

	// Método para conseguir los nodos AcceptEventAction y SendSignal Action para
	// mutar
	private static ArrayList<Node> getAcceptAndSendSignNodes(Document umlMutant, Entry<Node, Node> entry) {
		String[] expr = { entry.getKey().getAttributes().getNamedItem("xmi:id").getTextContent(),
				entry.getValue().getAttributes().getNamedItem("xmi:id").getTextContent() };
		ArrayList<Node> nodes = new ArrayList<>();
		for (String id : expr) {
			System.out.println(id);
			String exprNode1 = "//node[@id=\"" + id + "\"]";
			NodeList nodeConstrained = dp.evaluateExpresion(umlMutant, exprNode1);
			nodes.add(nodeConstrained.item(0));
		}

		return nodes;
	}

	// Método para cambiar el atribute 'name' del OwnedRule que especifíca la puerta
	// de 2 qubits
	private static void changeOwnedRuleName(Node ownedRule, String qGateReplace) {
		String previousName = ownedRule.getAttributes().getNamedItem("name").getTextContent();
		String newName = qGateReplace.toUpperCase()
				+ previousName.substring(previousName.indexOf("("), previousName.length());
		ownedRule.getAttributes().getNamedItem("name").setNodeValue(newName);
	}

	// Método para devolver el Node de la ownedRule de las puertas que vamos a
	// cambiar
	private static Node getOwnedRule(Document umlMutant, String idConstrainedElements) {
		String constrainedElementExpr = "//ownedRule[@constrainedElement=\"" + idConstrainedElements + "\"]";
		NodeList constrainedElementNodeLists = dp.evaluateExpresion(umlMutant, constrainedElementExpr);
		System.out.println(constrainedElementNodeLists.getLength());

		return constrainedElementNodeLists.item(0);
	}

	private static void saveMutants(ArrayList<Document> mutants, String pathToSave) {
		for (int i = 0; i < mutants.size(); i++) {
			changeNamePackagedElement(mutants.get(i), pathToSave.substring(pathToSave.length() - 9) + i);
			String path = pathToSave + i + ".uml";
			dp.saveFile(mutants.get(i), path);
		}
	}

	// Método para cambiar el Activity Model para generar varios Qiskit Programs
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
			insertNewEdgeQGI(umlMutant, idNextQg, "idMutant");

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
				insertNewEdgeQGI(umlMutant, idNextQg, "idMutant");

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

		// Creamos el elemento <QuantumUMLProfile:QuantumGate> para indicar que se crea
		// una nueva puerta cuántica con el QUML Profile
		Element qUmlProfilElement = umlMutant.createElement("QuantumUMLProfile:QuantumGate");
		qUmlProfilElement.setAttribute("xmi:id", "qUmlProfile");
		qUmlProfilElement.setAttribute("base_Action", "idMutant");

		// Añadimos los dos elementos que hemos creado al umlMutant
		umlMutant.getElementsByTagName("node").item(0).getParentNode().appendChild(newEdge);
		umlMutant.getElementsByTagName("QuantumUMLProfile:QuantumGate").item(0).getParentNode()
				.appendChild(qUmlProfilElement);

	}

	// Creamos el edge que va a conectar la nueva puerta con la siguiente puerta
	private static void insertNewEdgeQGI(Document umlMutant, String idNextQg, String idMutant) {
		Element newEdge = umlMutant.createElement("edge");
		newEdge.setAttribute("xmi:id", "edgeMutant");
		newEdge.setAttribute("xmi:type", "uml:ControlFlow");
		newEdge.setAttribute("target", idNextQg);
		newEdge.setAttribute("source", idMutant);
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

	// Método para conseguir las puertas cuánticas de un único qubit
	private static ArrayList<Node> getOneQubitGatesNodes(Document umlDocument) {
		String nodes = "//node";
		ArrayList<Node> qGatesNodes = new ArrayList<>();
		NodeList qGatesUml = dp.evaluateExpresion(umlDocument, nodes);

		for (int i = 0; i < qGatesUml.getLength(); i++) {
			NamedNodeMap attributes = qGatesUml.item(i).getAttributes();

			if (attributes.getNamedItem("xmi:type").getTextContent()
					.equals(UmlEnum.ONE_QUBIT_QUANTUM_GATE.getTypes()[0])) {
				qGatesNodes.add(qGatesUml.item(i));
			}
		}

		return qGatesNodes;
	}

	// Método para conseguir los nodes de las puertas cuánticas de dos qubits
	private static HashMap<Node, Node> getTwoQubitGatesNodes(Document umlDocument) {
		String nodes = "//node";
		ArrayList<Node> qGatesNodes = new ArrayList<>();
		HashMap<Node, Node> constrainedQuantumGates = new HashMap<Node, Node>();

		NodeList qGatesUml = dp.evaluateExpresion(umlDocument, nodes);

		for (int i = 0; i < qGatesUml.getLength(); i++) {
			NamedNodeMap attributes = qGatesUml.item(i).getAttributes();
			for (String xmiType : UmlEnum.TWO_QUBIT_QUANTUM_GATE.getTypes()) {
				if (attributes.getNamedItem("xmi:type").getTextContent()
						.equals(xmiType)) {
					qGatesNodes.add(qGatesUml.item(i));
				}
			}
		}

		NodeList constrainedElements = getConstrainedElements(umlDocument);
		for (int i = 0; i < constrainedElements.getLength(); i++) {
			String[] idsConstrained = getIdConstrainedElements(constrainedElements.item(i)).split(" ");

			String firstNodeEvaluateExpr = "//node[@id=\"" + idsConstrained[0] + "\"]";
			String secondNodeEvaluateExpr = "//node[@id=\"" + idsConstrained[1] + "\"]";

			NodeList firstConstrainedGate = dp.evaluateExpresion(umlDocument, firstNodeEvaluateExpr);
			NodeList secondConstrainedGate = dp.evaluateExpresion(umlDocument, secondNodeEvaluateExpr);

			constrainedQuantumGates.put(firstConstrainedGate.item(0), secondConstrainedGate.item(0));
		}

		return constrainedQuantumGates;
	}

	// Método para obtener los contrainedElements
	private static NodeList getConstrainedElements(Document umlDocument) {
		String ownedRule = "//ownedRule";
		NodeList qGatesUml = dp.evaluateExpresion(umlDocument, ownedRule);
		return qGatesUml;
	}

	// Método para obtener los ids de las puertas cuanticas constrained
	private static String getIdConstrainedElements(Node constrainedElement) {
		String ids = constrainedElement.getAttributes().getNamedItem("constrainedElement").getTextContent();

		return ids;
	}
}
