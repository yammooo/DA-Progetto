package simulation;

import data.models.SimulationParameters;

public class Simulation {
    SimulationParameters _params;
    boolean _isDebug;
    boolean _areExtraArgsRequired;
    
    public Simulation(SimulationParameters params, boolean areExtraArgsRequired, boolean isDebug) {
        _params = params;
        _isDebug = isDebug;
        _areExtraArgsRequired = areExtraArgsRequired;
    }

    public void run(){
        if(_isDebug) System.out.println("Simulation running...");
    }

}
