package simulation.models;

public class ArrivalEventValue extends EventValue {

    public ArrivalEventValue(int categoryNumber) {
        _categoryNumber = categoryNumber;
    }

    @Override
    public boolean isArrival() {
        return true;
    }
}