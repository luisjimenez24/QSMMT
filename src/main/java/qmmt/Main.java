package qmmt;

import java.util.Scanner;

import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;

import qmmt.controller.ModelMutantGenerator;
import qmmt.controller.QiskitMutantsGenerator;
import qmmt.utils.DefaultParser;

public class Main {
	static DefaultParser dp;
	static String routeInputModel = "umlModels\\prueba.uml";

	public static void main(String[] args) {
		menu();
	}

	private static void menu() {
		System.out.println("---Quantum Software Model Mutant Generator---");
		Scanner reader = new Scanner(System.in);

		boolean opt = true;
		while(opt){
			options();
			int aux = reader.nextInt();
			switch(aux){
				case 1:
				ModelMutantGenerator.initializeModelMutantGenerator(routeInputModel);
					break;
				case 2:
					try {
						QiskitMutantsGenerator.executeGeneratorActivity(routeInputModel);
					} catch (EolModelElementTypeNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
			}
		}

	}

	private static void options() {
			System.out.println("Select the option in the following menu");
			System.out.println("1 create mutants");
			System.out.println("2 create uml models");
	}



}
