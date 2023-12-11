package simulation.models;

public abstract class EventValue {
    protected int _categoryNumber;
    protected double _serviceTime = (double) 0.0;

    public abstract boolean isArrival();

    public int getCategory() {
        return _categoryNumber;
    }

    public double getServiceTime() {
        return _serviceTime;
    }
}