package simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import data.models.SimulationParameters;
import simulation.models.Event;
import simulation.models.Server;
import simulation.models.ArrivalEventValue;
import simulation.models.EndOfExecutionEventValue;

public class Simulation {
    private SimulationParameters _params;
    private boolean _isDebug;
    private boolean _areExtraArgsRequired;
    private PriorityQueue<Event> _Q;
    private ArrayList<Server> _servers;

    private String _endl = System.lineSeparator();
    
    public Simulation(SimulationParameters params, boolean areExtraArgsRequired, boolean isDebug) {
        _params = params;
        _isDebug = isDebug;
        _areExtraArgsRequired = areExtraArgsRequired;
        _Q = new PriorityQueue<Event>();
        _servers = new ArrayList<Server>();

        // Initialize each server with an empty queue of events
        for(int i = 0; i < _params.getParam().get("K"); i++) {
            _servers.add(new Server());
        }

    }

    public void run(){
        if (_isDebug) System.out.println("Simulation running...");

        int arrivalsProcessed = 0;

        // Q is initialized with the H events representing the arrivals of the first jobs of the various categories
        for(int categoryNumber = 0; categoryNumber < _params.getParam().get("H"); categoryNumber++){
            double time = _params.getExpDistGenerators().get(categoryNumber).nextArrivalTime();
            _Q.add(Event.createArrivalEvent(time, categoryNumber));
            if (_isDebug) System.out.println("Initial job created: category " + categoryNumber + ", time " + time);
        }

        // Execute a loop where in each iteration, an entry 'e' with the minimum key is extracted from 'Q' and processed
        // The type of processing depends on the type of event that 'e' represents
        for(int i=0; !_Q.isEmpty(); i++) {

            Event removedEvent = _Q.remove(); // An entry e(te, ve) with the minimum key is extracted from Q
            int categoryOfRemoved = removedEvent.getValue().getCategoryNumber();
            double timeOfRemoved = removedEvent.getTime();

            if(_isDebug) System.out.println("Processing event " + i + ": category " + categoryOfRemoved + ", time " + timeOfRemoved + ", isArrival " + removedEvent.getValue().isArrival());

            // If e represents the arrival of a job
            // Makes sure to process only the first N jobs extracted
            if(removedEvent.isValueArrival() && arrivalsProcessed < _params.getParam().get("N")) {

                // A new entry representing the arrival of the next job of category Cr is added to Q
                double newTime = timeOfRemoved + _params.getExpDistGenerators().get(categoryOfRemoved).nextArrivalTime();
                _Q.add(Event.createArrivalEvent(newTime, categoryOfRemoved));
                if (_isDebug) System.out.println("New job created: category " + categoryOfRemoved + ", time " + newTime);

                // A server S is selected for job J according to a scheduling policy
                int selectedServer = roundRobinPolicy(arrivalsProcessed++);

                // If S is busy, J is put in the FIFO queue of S
                // Otherwise J is scheduled for immediate execution in S
                if(_servers.get(selectedServer).isBusy()) {
                    _servers.get(selectedServer).pushEvent(removedEvent);
                    if (_isDebug) System.out.println("Server " + selectedServer + " is busy. Job added to queue.");
                } else {
                    // A new entry is added to Q, which represents the end of the execution of J at time te + se
                    double serviceTime = _params.getExpDistGenerators().get(categoryOfRemoved).nextServiceTime();
                    _Q.add(Event.createEndOfExecutionEvent(timeOfRemoved + serviceTime, categoryOfRemoved, selectedServer, serviceTime));
                    _servers.get(selectedServer).setBusy();
                    if (_isDebug) System.out.println("Server " + selectedServer + " is available. Job scheduled for execution with service time " + serviceTime + ".");
                }

                if(_areExtraArgsRequired) System.out.print(timeOfRemoved + "," + removedEvent.getServiceTime() + "," + categoryOfRemoved + _endl);

            } else if (!removedEvent.isValueArrival()) {
                int eventServer = ((EndOfExecutionEventValue)removedEvent.getValue()).getServerNumber();

                _servers.get(eventServer).trySetAvailable();

                // If the FIFO queue of S is not empty, the first job J' is removed from this queue
                // J' is scheduled for immediate execution in S, inserting in Q a new entry which represents the end of the execution of J'
                if(!_servers.get(eventServer).isFIFOEmpty()) {
                    Event removedFromFIFO = _servers.get(eventServer).popEvent();
                    double serviceTime = _params.getExpDistGenerators().get(removedFromFIFO.getValue().getCategoryNumber()).nextServiceTime();
                    _Q.add(Event.createEndOfExecutionEvent(timeOfRemoved + serviceTime, removedFromFIFO.getValue().getCategoryNumber(), eventServer, serviceTime));
                    _servers.get(eventServer).setBusy();
                    if (_isDebug) System.out.println("Job removed from queue of server " + eventServer + " and scheduled for execution. New end of execution event created with time " + (timeOfRemoved + serviceTime) + ".");
                }

                if(_areExtraArgsRequired) System.out.print(timeOfRemoved + "," + removedEvent.getServiceTime() + "," + categoryOfRemoved + _endl);
            }
            if(_isDebug) System.out.print(_endl);
        }
    }

    int roundRobinPolicy(int i) {
        return i % _params.getParam().get("K");
    }

}
