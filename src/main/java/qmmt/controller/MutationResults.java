package qmmt.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

//Clase para calcular los resultados de la mutación
public class MutationResults {

    // Método que inicializa la clase de MutationResults
    public static void calculate() {
        // Primero se ejecuta el fichero original y se obtienen los resultados

       execute(System.getProperty("user.dir") + "\\output\\Qiskit\\original.py");
        // Después se ejecutan el resto de ficheros mutantes y se obtienen
    }

    public static void execute(String fileName) {
        ProcessBuilder processBuilder = new ProcessBuilder("python", fileName);
        processBuilder.redirectErrorStream(true);

        Process process;
        int exitCode;
        try {
            process = processBuilder.start();
            List<String> results = readProcessOutput(process.getInputStream());
          System.out.println(results.get(0));
            exitCode= process.waitFor();
            System.out.println("salida: " + exitCode);

        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

         

    }

    private static List<String> readProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        }
    }
}
