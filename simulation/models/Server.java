package simulation.models;

import java.util.LinkedList;
import java.util.Queue;

public class Server {
    
    private Queue<Event> _server;
    private boolean _isBusy;

    public Server() {
        _server = new LinkedList<Event>();
        _isBusy = false;
    }

    public void pushEvent(Event event) {
        _server.add(event);
        _isBusy = true;
    }

    public Event popEvent() {
        return _server.remove();
    }

    public boolean isFIFOEmpty() {
        return _server.isEmpty();
    }

    public boolean isBusy() {
        return _isBusy;
    }

    public void trySetAvailable() {
        _isBusy = false;
       if(!isFIFOEmpty()) _isBusy = true;
    }

    public void setBusy() {
        _isBusy = true;
    }
    
}
