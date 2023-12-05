import data.handlers.SimulationDataHandler;
import data.models.ExpDistGenerator;
import data.models.SimulationParameters;
import simulation.Simulation;

public class Simulator {
    public static void main(String args[]){

        boolean isDebug = false;

        SimulationParameters params = SimulationDataHandler.readFileData("Simulator parameters.txt");
        if (isDebug) {
            System.out.println(params);
        } else {
            System.out.print(params.getParam().get("K") + ",");
            System.out.print(params.getParam().get("H") + ",");
            System.out.print(params.getParam().get("N") + ",");
            System.out.print(params.getParam().get("R") + ",");
            System.out.println(params.getParam().get("P"));
        }


        boolean areExtraArgsRequired = SimulationDataHandler.areExtraArgsRequired(params);

        for(int i = 0; i < params.getParam().get("R"); i++){
            Simulation simulation = new Simulation(params, areExtraArgsRequired, isDebug);
            simulation.run();
        }
    }
}