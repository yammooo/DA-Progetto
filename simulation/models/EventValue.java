package simulation.models;

public abstract class EventValue {
    protected int _categoryNumber;
    protected float _serviceTime = (float) 0.0;

    public abstract boolean isArrival();

    public int getCategoryNumber() {
        return _categoryNumber;
    }

    public float getServiceTime() {
        return _serviceTime;
    }
}