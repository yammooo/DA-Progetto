package data.handlers;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import data.models.ExpDistGenerator;
import data.models.SimulationParameters;

public class SimulationDataHandler{

    public static SimulationParameters readFileData(String fileName){
        Map<String, Integer> param = new HashMap<String, Integer>();
        List<ExpDistGenerator> expDistGenerators = new ArrayList<ExpDistGenerator>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            String[] parts = line.split(",");
            param.put("K", Integer.parseInt(parts[0])); // K = number of servers
            param.put("H", Integer.parseInt(parts[1])); // H = number of categories
            param.put("N", Integer.parseInt(parts[2])); // N = total number of jobs to be simulated
            param.put("R", Integer.parseInt(parts[3])); // R = repetitions of the simulation
            param.put("P", Integer.parseInt(parts[4])); // P = the type of scheduling policy to use

            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                parts = line.split(",");
                float lambdaArrival = Float.parseFloat(parts[0]);
                float lambdaService = Float.parseFloat(parts[1]);
                int seedArrival = Integer.parseInt(parts[2]);
                int seedService = Integer.parseInt(parts[3]);
                expDistGenerators.add(new ExpDistGenerator(lambdaArrival, lambdaService, seedArrival, seedService));
            }

            if (lineCount != param.get("H")) {
                throw new IOException("Invalid number of lines in file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        }

        return new SimulationParameters(param, expDistGenerators);
    }

}