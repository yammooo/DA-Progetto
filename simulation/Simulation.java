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
        if (_isDebug) System.out.println("Simulation running...");

        int jobsCreated = 0;

        // Q is initialized with the H events representing the arrivals of the first jobs of the various categories
        for(int categoryNumber = 0; categoryNumber < _params.getParam().get("H"); categoryNumber++){
            float time = _params.getExpDistGenerators().get(categoryNumber).nextArrivalTime();
            _Q.add(new Event(time, true, categoryNumber));
        }

        jobsCreated += _params.getParam().get("H");

        // Execute a loop where in each iteration, an entry 'e' with the minimum key is extracted from 'Q' and processed
        // The type of processing depends on the type of event that 'e' represents
        for(int i=0; !_Q.isEmpty(); i++) {

            Event removedEvent = _Q.remove();
            int categoryOfRemoved = removedEvent.getValue().getCategoryNumber();
            float timeofRemoved = removedEvent.getTime();
            float serviceTime = (float) 0.0;

            if(_isDebug) System.out.println("Processing event " + i + ": category " + categoryOfRemoved + ", time " + timeofRemoved + ", isArrival " + removedEvent.getValue().isArrival());

            if(removedEvent.getValue().isArrival()) {

                if(jobsCreated < _params.getParam().get("N")){
                    _Q.add(new Event(timeofRemoved + _params.getExpDistGenerators().get(categoryOfRemoved).nextArrivalTime(), true, categoryOfRemoved));
                    jobsCreated++;
                }

                int selectedServer = roundRobinPolicy(i); // Implement for P = 1

                if(!_servers.get(selectedServer).isEmpty()) { // If S is busy
                    _servers.get(selectedServer).add(removedEvent);
                } else {
                    _Q.add(new Event(timeofRemoved + _params.getExpDistGenerators().get(categoryOfRemoved).nextServiceTime(), false, categoryOfRemoved, selectedServer));
                }

            } else {
                int eventServer = removedEvent.getValue().getServerNumber();

                if(!_servers.get(eventServer).isEmpty()) {
                    Event removedFromFIFO = _servers.get(eventServer).remove();
                    serviceTime = _params.getExpDistGenerators().get(removedFromFIFO.getValue().getCategoryNumber()).nextServiceTime();
                    _Q.add(new Event(removedFromFIFO.getTime() + serviceTime, false, removedFromFIFO.getValue().getCategoryNumber(), eventServer));
                }

            }

            
            if(!_isDebug && _areExtraArgsRequired) {
                System.out.print(timeofRemoved + ",");
                System.out.print(serviceTime + ",");
                System.out.println(categoryOfRemoved + ",");
            }
            
        }
    }

    int roundRobinPolicy(int i) {
        return i % _params.getParam().get("K");
    }

}
