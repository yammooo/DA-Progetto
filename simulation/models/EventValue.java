package simulation.models;

public class EventValue {
    private boolean _isArrival;
    private int _categoryNumber;
    private int _serverNumber = -1;

    public EventValue(boolean isArrival, int categoryNumber) {
        _isArrival = isArrival;
        _categoryNumber = categoryNumber;
    }

    public EventValue(boolean isArrival, int categoryNumber, int serverNumber) {
        _isArrival = isArrival;
        _categoryNumber = categoryNumber;
        _serverNumber = serverNumber;
    }

    public boolean isArrival() {
        return _isArrival;
    }

    public boolean isEndOfExecution() {
        return !_isArrival;
    }

    public int getCategoryNumber() {
        return _categoryNumber;
    }

    public int getServerNumber() {
        return _serverNumber;
    }

    public void setServerNumber(int serverNumber) {
        _serverNumber = serverNumber;
    }
}
