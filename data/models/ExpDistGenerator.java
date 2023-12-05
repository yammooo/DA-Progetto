package data.models;

import java.util.Random;

public class ExpDistGenerator {
    private Random _randomArrival;
    private Random _randomService;
    private float _lambdaArrival;
    private float _lambdaService;
    private long _seedArrival;
    private long _seedService;

    public ExpDistGenerator(float lambdaArrival, float lambdaService, long seedArrival, long seedService) {
        _lambdaArrival = lambdaArrival;
        _lambdaService = lambdaService;
        _seedArrival = seedArrival;
        _seedService = seedService;
        _randomArrival = new Random(_seedArrival);
        _randomService = new Random(_seedService);
    }

    public float nextArrivalTime() {
        return (float) (-Math.log(1 - _randomArrival.nextFloat()) / _lambdaArrival);
    }

    public float nextServiceTime() {
        return (float) (-Math.log(1 - _randomService.nextFloat()) / _lambdaService);
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