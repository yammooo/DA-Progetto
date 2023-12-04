package simulation.models;

public class Entry {
    private float _time;
    private Value _value;

    public Entry(float time, Value value) {
        _time = time;
        _value = value;
    }

    public float getTime() {
        return _time;
    }

    public Value getValue() {
        return _value;
    }
}
