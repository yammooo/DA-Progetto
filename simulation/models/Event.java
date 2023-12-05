package simulation.models;

public class Event implements Comparable<Event> {
    private float _time;
    private EventValue _value;

    private Event(float time, EventValue value) {
        _time = time;
        _value = value;
    }

    public static Event createArrivalEvent(float time, int categoryNumber, int arrivalNumber) {
        return new Event(time, new ArrivalEventValue(categoryNumber, arrivalNumber));
    }

    public static Event createEndOfExecutionEvent(float time, int categoryNumber, int serverNumber, float serviceTime) {
        return new Event(time, new EndOfExecutionEventValue(categoryNumber, serverNumber, serviceTime));
    }

    public float getTime() {
        return _time;
    }

    public float getServiceTime() {
        return _value.getServiceTime();
    }

    public EventValue getValue() {
        return _value;
    }

    public boolean isValueArrival() {
        return _value.isArrival();
    }

    @Override
    public int compareTo(Event other) {
        return Float.compare(this._time, other._time);
    }
}
