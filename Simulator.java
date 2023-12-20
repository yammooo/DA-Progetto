import data.handlers.SimulationParamsHandler;
import data.models.SimulationParameters;
import simulation.Simulation;
import simulation.models.SimulationData;
import simulation.models.SimulationsData;

/*

A brief high-level description of the new policy:

I choose the server that is expected to be free the soonest.

Here is how i calculate the expected time of a server to be free:

1) I look at the server's queue of jobs waiting to be executed (excluding the job it's currently working on).
    I calculate the expected time to finish these jobs by adding up their expected service times (which is 1/λ).

2) I add to this the remaining execution time of the current job being processed at that server.

*/

public class Simulator {
    public static void main(String args[]){

        boolean isDebug = false;

        // Reads the simulation parameters from a file
        SimulationParameters params = SimulationParamsHandler.readFileData("Simulator parameters.txt");

        // Object that stores the data from the R simulations.
        SimulationsData simulationsData = new SimulationsData(params);

        // Runs the simulation R times.
        for(int i = 0; i < params.getParam().get("R"); i++){
            // Creates a new simulation object with the given parameters.
            Simulation simulation = new Simulation(params, isDebug);

            // Runs the simulation and gets the data.
            SimulationData data = simulation.run();
            
            // Adds the data from this simulation to simulationsData.
            simulationsData.addSimulationData(data);
        }

        // Prints the required arguments.
        System.out.print(simulationsData);
    }
}