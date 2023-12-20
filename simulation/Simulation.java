package simulation;

import java.util.ArrayList;
import java.util.PriorityQueue;
import data.models.SimulationParameters;
import simulation.models.Event;
import simulation.models.Server;
import simulation.models.SimulationData;
import simulation.models.EndOfExecutionEventValue;

public class Simulation {
    private SimulationParameters _params;
    private boolean _isDebug;
    private PriorityQueue<Event> _Q;
    private ArrayList<Server> _servers;
    private SimulationData _simulationData;
    
    public Simulation(SimulationParameters params, boolean isDebug) {
        _params = params;
        _isDebug = isDebug;
        _Q = new PriorityQueue<Event>();
        _servers = new ArrayList<Server>();
        _simulationData = new SimulationData(_params);

        // Initialize each server with an empty queue of events
        for(int i = 0; i < _params.getParam().get("K"); i++) {
            _servers.add(new Server(_params));
        }

    }

    public SimulationData run(){
        String endl = System.lineSeparator();

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
            int categoryOfRemoved = removedEvent.getValue().getCategory();
            double timeOfRemoved = removedEvent.getTime();

            if(_isDebug) System.out.println("Processing event " + i + ": category " + categoryOfRemoved + ", time " + timeOfRemoved + ", isArrival " + removedEvent.getValue().isArrival());

            // If e represents the arrival of a job
            // Makes sure to process only the first N jobs extracted
            if(removedEvent.isArrival() && arrivalsProcessed < _params.getParam().get("N")) {

                // A new entry representing the arrival of the next job of category Cr is added to Q
                double newTime = timeOfRemoved + _params.getExpDistGenerators().get(categoryOfRemoved).nextArrivalTime();
                _Q.add(Event.createArrivalEvent(newTime, categoryOfRemoved));
                if (_isDebug) System.out.println("New job created: category " + categoryOfRemoved + ", time " + newTime);

                // A server S is selected for job J according to a scheduling policy
                int selectedServerByPolicy;
                if(_params.getParam().get("P") == 0){
                    selectedServerByPolicy = roundRobinPolicy(arrivalsProcessed);
                } else {
                    selectedServerByPolicy = customPolicy(removedEvent.getTime());
                }
                arrivalsProcessed++;

                // If S is busy, J is put in the FIFO queue of S
                // Otherwise J is scheduled for immediate execution in S
                if(_servers.get(selectedServerByPolicy).isBusy()) {
                    _servers.get(selectedServerByPolicy).addEvent(removedEvent);
                    if (_isDebug) System.out.println("Server " + selectedServerByPolicy + " is busy. Job added to queue.");
                } else {
                    // A new entry is added to Q, which represents the end of the execution of J at time te + se
                    double serviceTime = _params.getExpDistGenerators().get(categoryOfRemoved).nextServiceTime();
                    Event endOfExecutionEvent = Event.createEndOfExecutionEvent(timeOfRemoved + serviceTime, categoryOfRemoved, selectedServerByPolicy, serviceTime);
                    _Q.add(endOfExecutionEvent);
                    _servers.get(selectedServerByPolicy).setBusy(endOfExecutionEvent);
                    _simulationData.addJob(timeOfRemoved, timeOfRemoved, serviceTime, categoryOfRemoved);
                    if (_isDebug) System.out.println("Server " + selectedServerByPolicy + " is available. Job scheduled for execution with service time " + serviceTime + ".");
                }

                _simulationData.addExtraArg(timeOfRemoved, removedEvent.getServiceTime(), categoryOfRemoved);

            } else if (!removedEvent.isArrival()) {
                int serverOfRemovedEvent = ((EndOfExecutionEventValue)removedEvent.getValue()).getServerNumber();

                _servers.get(serverOfRemovedEvent).trySetAvailable();

                // If the FIFO queue of S is not empty, the first job J' is removed from this queue
                // J' is scheduled for immediate execution in S, inserting in Q a new entry which represents the end of the execution of J'
                if(!_servers.get(serverOfRemovedEvent).isFIFOEmpty()) {
                    Event removedFromFIFO = _servers.get(serverOfRemovedEvent).removeEvent();
                    double serviceTime = _params.getExpDistGenerators().get(removedFromFIFO.getValue().getCategory()).nextServiceTime();
                    Event endOfExecutionEvent = Event.createEndOfExecutionEvent(timeOfRemoved + serviceTime, removedFromFIFO.getValue().getCategory(), serverOfRemovedEvent, serviceTime);
                    _Q.add(endOfExecutionEvent);
                    _servers.get(serverOfRemovedEvent).setBusy(endOfExecutionEvent);
                    _simulationData.addJob(removedFromFIFO.getTime(), timeOfRemoved, serviceTime, removedFromFIFO.getCategory());
                    if (_isDebug) System.out.println("Job removed from queue of server " + serverOfRemovedEvent + " and scheduled for execution. New end of execution event created with time " + (timeOfRemoved + serviceTime) + ".");
                }

                _simulationData.addExtraArg(timeOfRemoved, removedEvent.getServiceTime(), categoryOfRemoved);

                // Set the end time of the simulation to the time when the last job ends its execution
                _simulationData.setEndTime(timeOfRemoved);
            }
            
            if(_isDebug) System.out.print(endl);
        }

        return _simulationData;
    }

    int roundRobinPolicy(int i) {
        return i % _params.getParam().get("K");
    }

    int customPolicy(double currentTime) {
        int minIndex = 0;
        double minServiceTime = _servers.get(minIndex).getExpFIFOServiceTime() + Math.max(0, _servers.get(minIndex).getExecutingJobTime() - currentTime);

        for (int next = 1; next < _servers.size(); next++) {
            if (!_servers.get(minIndex).isBusy()) return minIndex;

            double nextServiceTime = _servers.get(next).getExpFIFOServiceTime() + Math.max(0, _servers.get(next).getExecutingJobTime() - currentTime);
            
            if (nextServiceTime < minServiceTime) {
                minIndex = next;
                minServiceTime = nextServiceTime;
            }
        }
        return minIndex;
    }

}
