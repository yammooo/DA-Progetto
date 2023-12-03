package data.models;

public class Category {
    private double _lambdaArrival;
    private double _lambdaService;
    private int _seedArrival;
    private int _seedService;

    public Category(double lambdaArrival, double lambdaService, int seedArrival, int seedService) {
        this._lambdaArrival = lambdaArrival;
        this._lambdaService = lambdaService;
        this._seedArrival = seedArrival;
        this._seedService = seedService;
    }

    public double getLambdaArrival() {return _lambdaArrival;}
    public double getLambdaService() {return _lambdaService;}
    public int getSeedArrival() {return _seedArrival;}
    public int getSeedService() {return _seedService;}

    @Override
    public String toString(){
        return String.format("Category{lambdaArrival=%s, lambdaService=%s, seedArrival=%s, seedService=%s}", _lambdaArrival, _lambdaService, _seedArrival, _seedService);
    }
}