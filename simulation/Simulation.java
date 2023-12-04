package simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import data.models.SimulationParameters;
import simulation.models.Event;

public class Simulation {
    private SimulationParameters _params;
    private boolean _isDebug;
    private boolean _areExtraArgsRequired;
    private PriorityQueue<Event> _Q;
    private ArrayList<Queue<Event>> _servers;
    
    public Simulation(SimulationParameters params, boolean areExtraArgsRequired, boolean isDebug) {
        _params = params;
        _isDebug = isDebug;
        _areExtraArgsRequired = areExtraArgsRequired;
        _Q = new PriorityQueue<Event>();
        _servers = new ArrayList<Queue<Event>>();

        // Initialize each server with an empty queue of events
        for(int i = 0; i < _params.getParam().get("K"); i++) {
            _servers.add(new LinkedList<Event>());
        }

    }

    public void run(){
        if(_isDebug) System.out.println("Simulation running...");

        // Q is initialized with the H events representing the arrivals of the first jobs of the various categories
        for(int categoryNumber = 0; categoryNumber < _params.getParam().get("H"); categoryNumber++){
            float time = _params.getExpDistGenerators().get(categoryNumber).nextArrivalTime();
            _Q.add(new Event(time, true, categoryNumber));
        }

        // Execute a loop where in each iteration, an entry 'e' with the minimum key is extracted from 'Q' and processed
        // The type of processing depends on the type of event that 'e' represents
        for(int i=0; i < _params.getParam().get("N"); i++) {

            Event removedEvent = _Q.remove();
            int categoryNumber = removedEvent.getValue().getCategoryNumber();
            float time = removedEvent.getTime();

            if(_isDebug) System.out.println("Processing event " + i + ": category " + categoryNumber + ", time " + time);

            if(removedEvent.getValue().isArrival()) {

                _Q.add(new Event(time + _params.getExpDistGenerators().get(categoryNumber).nextArrivalTime(), true, categoryNumber));
                
                int selectedServer = roundRobinPolicy(i); // Implement for P = 1

                if(_servers.get(selectedServer).isEmpty()) {
                    _servers.get(selectedServer).add(removedEvent);
                } else {
                    _Q.add(new Event(time + _params.getExpDistGenerators().get(categoryNumber).nextServiceTime(), false, categoryNumber, selectedServer));
                }

            } else {

                int eventServer = removedEvent.getValue().getServerNumber();

                if(!_servers.get(eventServer).isEmpty()) {
                    _Q.add(new Event(time + _params.getExpDistGenerators().get(categoryNumber).nextServiceTime(), false, categoryNumber, eventServer));
                }

            }

        }
    }

    int roundRobinPolicy(int i) {
        return i % _params.getParam().get("K");
    }

}
