package qmmt;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;

import qmmt.controller.ModelMutantGenerator;
import qmmt.controller.QiskitMutantsGenerator;
import qmmt.utils.DefaultParser;

public class Main {
	static DefaultParser dp;
	static final String routeInputModel = "umlModels\\pruebacx.uml";
	static final Scanner reader = new Scanner(System.in);

	public static void main(String[] args) {
		menu();
	}

	//First menu of the application: 1) to generate UML mutants; 2) to generate Qiskit programs from UML models
	private static void menu() {
		System.out.println("---Quantum Software Model Mutant Generator---");
		boolean opt = true;
		while (opt) {
			options();
			int aux = reader.nextInt();
			switch (aux) {
				case 1:
					ModelMutantGenerator.initializeModelMutantGenerator(routeInputModel);
					System.out.println("UML Mutants generated");
					break;
				case 2:
					ArrayList<String> modelsToQiskit = new ArrayList<>();
					mutantsToGenerate(modelsToQiskit);
					executeQiskitGenerator(modelsToQiskit);
					System.out.println("Qiskit programs generated");
					break;
			}

		}
		reader.close();
	}

	//Execute the Qiskit program generation to all the models from one folder
	private static void executeQiskitGenerator(ArrayList<String> modelsToQiskit) {
		for (String route : modelsToQiskit) {
			final File folder = new File(route);
			for (final File fileEntry : folder.listFiles()) {
				try {
					QiskitMutantsGenerator.executeGeneratorActivity(fileEntry.getAbsolutePath());
				} catch (EolModelElementTypeNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//Menu to choose the UML mutation approach (https://dl.acm.org/doi/pdf/10.1145/3533767.3543296)
	private static void mutantsToGenerate(ArrayList<String> approachesToGenerate) {
		System.out.println("From what approach you want to generate Qiskit programs?");
		boolean cont = true;
		while (cont) {
			displayOptionsMut();
			int option = reader.nextInt();
			switch (option) {
				case 1:
					approachesToGenerate.add("umlModels\\mutantsQGR");
					break;
				case 2:
					approachesToGenerate.add("umlModels\\mutantsQGD");
					break;
				case 3:
					approachesToGenerate.add("umlModels\\mutantsQGI");
					break;
				case 4:
					approachesToGenerate.add("umlModels\\mutantsQMI");
					break;
				case 5:
					approachesToGenerate.add("umlModels\\mutantsQMD");
					break;
				case 6:
					cont = false;
					break;
				}
			}
		}

	//Options of the second menu (to choose the UML mutatation approach)
	private static void displayOptionsMut() {
		System.out.println("1 - Quantum Gate Replacement");
		System.out.println("2 - Quantum Gate Deletion");
		System.out.println("3 - Quantum Gate Insertion");
		System.out.println("4 - Quantum Measurement Insertion");
		System.out.println("5 - Quantum Measurement Deletion");
		System.out.println("6 - Stop");

	}
	//Options of the first menu (to choose between mutate UML models or generate qiskit programs)
	private static void options() {
		System.out.println("Select the option in the following menu");
		System.out.println("1 - Create mutants");
		System.out.println("2 - Create qiskit programs");
		System.out.println("3 - Create test suite");
		System.out.println("4 - Execute test suite");
	}
}
