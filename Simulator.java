import data.handlers.SimulationDataHandler;
import data.models.ExpDistGenerator;
import data.models.SimulationParameters;

public class Simulator {
    public static void main(String args[]){

        boolean isDebug = true;

        SimulationParameters params = SimulationDataHandler.readFileData("Simulator parameters.txt");
        if (isDebug) System.out.println(params);

        
    }
}