package simulation.models;

public class EndOfExecutionEventValue extends EventValue {
    private int _serverNumber;

    public EndOfExecutionEventValue(int categoryNumber, int serverNumber, double serviceTime) {
        _categoryNumber = categoryNumber;
        _serverNumber = serverNumber;
        _serviceTime = serviceTime;
    }

    public int getServerNumber() {
        return _serverNumber;
    }

    @Override
    public boolean isArrival() {
        return false;
    }
}