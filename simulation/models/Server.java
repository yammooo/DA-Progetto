package simulation.models;

import java.util.LinkedList;
import java.util.Queue;

import data.models.SimulationParameters;

public class Server implements Comparable<Server> {
    
    private Queue<Event> _server;
    private boolean _isBusy;
    private SimulationParameters _params;
    private double _expFIFOServiceTime = 0.0;
    private Event _executingJob;

    public Server(SimulationParameters params) {
        _server = new LinkedList<Event>();
        _isBusy = false;
        _params = params;
    }

    public void addEvent(Event event) {
        _expFIFOServiceTime += (1/(_params.getExpDistGenerators().get(event.getCategory()).getLambdaService()));
        _server.add(event);
        _isBusy = true;
    }

    public Event removeEvent() {
        Event event = _server.remove();
        _expFIFOServiceTime -= (1/(_params.getExpDistGenerators().get(event.getCategory()).getLambdaService()));
        return event;
    }

    public boolean isFIFOEmpty() {
        return _server.isEmpty();
    }

    public boolean isBusy() {
        return _isBusy;
    }

    public void trySetAvailable() {
        if(isFIFOEmpty()){
            _executingJob = null;
            _isBusy = false;
        }
    }

    public void setBusy(Event executingJob) {
        _executingJob = executingJob;
        _isBusy = true;
    }

    public double getExpFIFOServiceTime() {
        return _expFIFOServiceTime;
    }

    public double getExecutingJobTime() {
        if(_executingJob == null) return 0.0;
        return _executingJob.getTime();
    }

    @Override
    public int compareTo(Server other) {
        return Double.compare(this.getExpFIFOServiceTime(), other.getExpFIFOServiceTime());
    }
    
}
