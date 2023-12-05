package simulation.models;

public class ArrivalEventValue extends EventValue {
    private int _arrivalNumber;

    public ArrivalEventValue(int categoryNumber, int arrivalNumber) {
        _categoryNumber = categoryNumber;
        _arrivalNumber = arrivalNumber;
    }

    public int getArrivalNumber() {
        return _arrivalNumber;
    }

    @Override
    public boolean isArrival() {
        return true;
    }
}