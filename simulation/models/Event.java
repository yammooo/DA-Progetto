package simulation.models;

public class Event implements Comparable<Event> {
    private double _time;
    private EventValue _value;

    private Event(double time, EventValue value) {
        _time = time;
        _value = value;
    }

    public static Event createArrivalEvent(double time, int categoryNumber) {
        return new Event(time, new ArrivalEventValue(categoryNumber));
    }

    public static Event createEndOfExecutionEvent(double time, int categoryNumber, int serverNumber, double serviceTime) {
        return new Event(time, new EndOfExecutionEventValue(categoryNumber, serverNumber, serviceTime));
    }

    public double getTime() {
        return _time;
    }

    public double getServiceTime() {
        return _value.getServiceTime();
    }

    public EventValue getValue() {
        return _value;
    }

    public boolean isArrival() {
        return _value.isArrival();
    }

    public int getCategory(){
        return _value.getCategory();
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this._time, other._time);
    }
}
