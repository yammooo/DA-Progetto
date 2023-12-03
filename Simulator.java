import data.handlers.SimulationDataHandler;
import data.models.Category;
import data.models.SimulationParameters;

public class Simulator {
    public static void main(String args[]){

        SimulationParameters params = SimulationDataHandler.readFileData("Simulator parameters.txt");
        System.out.println(params);
    }
}