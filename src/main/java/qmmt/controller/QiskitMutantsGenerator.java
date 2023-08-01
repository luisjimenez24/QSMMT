package qmmt.controller;

import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.uml.UmlModel;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;

import qmmt.utils.QiskitGeneratorUtils;

public class QiskitMutantsGenerator {
	private static String egxActivityFilePath = "src\\main\\java\\qmmt\\templates\\activityGen.egx"; // Path to egx activity file

    /**
     * Method that executes an egx file on uml activity file.
     * 
     * @param umlFilePath Path to uml activity file.
     * @return Returns name of activity (used for class transformation).
     * @throws EolModelElementTypeNotFoundException
     */
    public static String executeGeneratorActivity(String umlFilePath) throws EolModelElementTypeNotFoundException {
        EgxModule module = QiskitGeneratorUtils.parseEgxFile(egxActivityFilePath); // Parse egxFilePath.egx
        UmlModel umlModel = QiskitGeneratorUtils.loadUml(umlFilePath); // Load UmlModel

        module.getContext().getModelRepository().addModel(umlModel); // Make the document visible to the EGX program
        try {
            module.execute(); // Execute module egxFilePath.egx
        } catch (EolRuntimeException e) {
            e.printStackTrace();
            System.out.printf("Failed to execute %s%n", umlFilePath);
        }
        return umlModel.getAllOfType("Activity").toString().split("name: ")[1].split(",")[0].toLowerCase()
                .replace(' ', '_').replace('-', '_');
    }
}
