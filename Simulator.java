import data.handlers.SimulationDataHandler;
import data.models.ExpDistGenerator;
import data.models.SimulationParameters;
import simulation.Simulation;

public class Simulator {
    public static void main(String args[]){

        boolean isDebug = true;

        SimulationParameters params = SimulationDataHandler.readFileData("Simulator parameters.txt");
        if (isDebug) System.out.println(params);

        boolean areExtraArgsRequired = SimulationDataHandler.areExtraArgsRequired(params);

        for(int i = 0; i < params.getParam().get("R"); i++){
            Simulation simulation = new Simulation(params, areExtraArgsRequired, isDebug);
            simulation.run();
        }
    }
}