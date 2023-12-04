package simulation.models;

public class Value {
    private boolean _isArrival;
    private int _categoryNumber;

    public Value(boolean isArrival, int categoryNumber) {
        _isArrival = isArrival;
        _categoryNumber = categoryNumber;
    }

    public boolean isArrival() {
        return _isArrival;
    }

    public int getCategoryNumber() {
        return _categoryNumber;
    }
}
