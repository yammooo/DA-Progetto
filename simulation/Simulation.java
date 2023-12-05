package simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import data.models.SimulationParameters;
import simulation.models.Event;
import simulation.models.ArrivalEventValue;
import simulation.models.EndOfExecutionEventValue;

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
            _Q.add(Event.createArrivalEvent(time, categoryNumber, jobsCreated++));
        }

        // Execute a loop where in each iteration, an entry 'e' with the minimum key is extracted from 'Q' and processed
        // The type of processing depends on the type of event that 'e' represents
        for(int i=0; !_Q.isEmpty(); i++) {

            Event removedEvent = _Q.remove(); // An entry e(te, ve) with the minimum key is extracted from Q
            int categoryOfRemoved = removedEvent.getValue().getCategoryNumber();
            float timeOfRemoved = removedEvent.getTime();

            if(_isDebug) System.out.println("Processing event " + i + ": category " + categoryOfRemoved + ", time " + timeOfRemoved + ", isArrival " + removedEvent.getValue().isArrival());

            // If e represents the arrival of a job
            if(removedEvent.getValue().isArrival()) {

                // A new entry representing the arrival of the next job of category Cr is added to Q
                // Make sure that the number of jobs created is less than N
                if(jobsCreated < _params.getParam().get("N")){
                    float newTime = timeOfRemoved + _params.getExpDistGenerators().get(categoryOfRemoved).nextArrivalTime();
                    _Q.add(Event.createArrivalEvent(newTime, categoryOfRemoved, jobsCreated++));
                }

                // A server S is selected for job J according to a scheduling policy
                int selectedServer = roundRobinPolicy(((ArrivalEventValue)removedEvent.getValue()).getArrivalNumber());

                // If S is busy, J is put in the FIFO queue of S
                // Otherwise J is scheduled for immediate execution in S
                if(!_servers.get(selectedServer).isEmpty()) {
                    _servers.get(selectedServer).add(removedEvent);
                } else {
                    // A new entry is added to Q, which represents the end of the execution of J at time te + se
                    float serviceTime = _params.getExpDistGenerators().get(categoryOfRemoved).nextServiceTime();
                    _Q.add(Event.createEndOfExecutionEvent(timeOfRemoved + serviceTime, categoryOfRemoved, selectedServer, serviceTime));
                }

            } else {
                int eventServer = ((EndOfExecutionEventValue)removedEvent.getValue()).getServerNumber();

                // If the FIFO queue of S is not empty, the first job J' is removed from this queue
                // J' is scheduled for immediate execution in S, inserting in Q a new entry which represents the end of the execution of J'
                if(!_servers.get(eventServer).isEmpty()) {
                    Event removedFromFIFO = _servers.get(eventServer).remove();
                    float serviceTime = _params.getExpDistGenerators().get(removedFromFIFO.getValue().getCategoryNumber()).nextServiceTime();
                    _Q.add(Event.createEndOfExecutionEvent(removedFromFIFO.getTime() + serviceTime, removedFromFIFO.getValue().getCategoryNumber(), eventServer, serviceTime));
                }

            }

            
            if(!_isDebug && _areExtraArgsRequired) {
                System.out.print(timeOfRemoved + ",");
                System.out.print(removedEvent.getServiceTime() + ",");
                System.out.println(categoryOfRemoved + ",");
            }
            
        }
    }

    int roundRobinPolicy(int i) {
        return i % _params.getParam().get("K");
    }

}
