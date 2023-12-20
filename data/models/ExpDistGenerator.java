package data.models;

import java.util.Random;

public class ExpDistGenerator {
    private Random _randomArrival;
    private Random _randomService;
    private double _lambdaArrival;
    private double _lambdaService;
    private long _seedArrival;
    private long _seedService;

    public ExpDistGenerator(double lambdaArrival, double lambdaService, long seedArrival, long seedService) {
        _lambdaArrival = lambdaArrival;
        _lambdaService = lambdaService;
        _seedArrival = seedArrival;
        _seedService = seedService;
        _randomArrival = new Random(_seedArrival);
        _randomService = new Random(_seedService);
    }

    public double nextArrivalTime() {
        return (-Math.log(1 - _randomArrival.nextFloat()) / _lambdaArrival);
    }

    public double nextServiceTime() {
        return (-Math.log(1 - _randomService.nextFloat()) / _lambdaService);
    }

    public double getLambdaService() {
        return _lambdaService;
    }

    public double getLambdaArrival() {
        return _lambdaArrival;
    }

    @Override
    public String toString() {
        return "ExpDistGenerator{" +
            "lambdaArrival=" + _lambdaArrival +
            ", lambdaService=" + _lambdaService +
            ", seedArrival=" + _seedArrival +
            ", seedService=" + _seedService +
            '}';
    }
}