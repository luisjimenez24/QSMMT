package qmmt.utils;

import java.io.File;

import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.uml.UmlModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;

public class QiskitGeneratorUtils{
    private static String umlProfileFilePath = "umlModels\\profiles\\QuantumUMLProfile.profile.uml"; // Path to uml profile file

    	public static UmlModel loadUml(String umlFilePath) {
		UmlModel umlModel = new UmlModel();
		umlModel.setModelFile(umlFilePath);
		umlModel.setMetamodelFile(umlProfileFilePath); // Add the UML profile as a metamodel reference
		try {
			umlModel.load();
		} catch (EolModelLoadingException e) {
			e.printStackTrace();
			System.out.printf("[ERROR] Failed to load %s%n", umlFilePath);
		}
		return umlModel;
	}
    	public static EgxModule parseEgxFile(String egxFilePath) {
		EgxModule module = new EgxModule(new EglFileGeneratingTemplateFactory());
		try {
			module.parse(new File(egxFilePath).getAbsoluteFile());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.printf("[ERROR] Syntax errors found at %s%n", egxFilePath);
		}
		return module;
	}
	
}