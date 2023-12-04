package simulation.models;

public class Event implements Comparable<Event> {
    private float _time;
    private EventValue _value;

    public Event(float time, boolean isArrival, int categoryNumber) {
        _time = time;
        _value = new EventValue(isArrival, categoryNumber);
    }

    public Event(float time, boolean isArrival, int categoryNumber, int serverNumber) {
        _time = time;
        _value = new EventValue(isArrival, categoryNumber, serverNumber);
    }

    public Event(float time, EventValue value) {
        _time = time;
        _value = value;
    }

    public float getTime() {
        return _time;
    }

    public EventValue getValue() {
        return _value;
    }

    @Override
    public int compareTo(Event other) {
        return Float.compare(this._time, other._time);
    }
}
