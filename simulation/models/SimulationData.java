package simulation.models;

import data.models.SimulationParameters;

public class SimulationData {

    private String endl = System.lineSeparator();
    private double _endTime;
    private CategoryData[] _avgQueueTimePerCat;
    private boolean _areExtraArgsRequired;
    private StringBuilder _extraArgs;
    private int H;
    private int N;
    private int R;
    private int P;

    public SimulationData(SimulationParameters params) {
        _extraArgs = new StringBuilder();
        H = params.getParam().get("H");
        N = params.getParam().get("N");
        R = params.getParam().get("R");
        P = params.getParam().get("P");

        _areExtraArgsRequired = (R == 1 && N <= 10 && P == 0);
        _avgQueueTimePerCat = new CategoryData[H];
        for (int i = 0; i < H; i++) {
            _avgQueueTimePerCat[i] = new CategoryData();
        }
    }

    public void addJob(double arrivalTime, double executionTime, double serviceTime, int category) {
        _avgQueueTimePerCat[category]._simulatedJobs++;
        _avgQueueTimePerCat[category]._sumOfQueueingTime += (executionTime - arrivalTime);
        _avgQueueTimePerCat[category]._sumOfServiceTime += serviceTime;
    }

    public void addExtraArg(double timeOfOccurence, double serviceTime, int category){
        if (_areExtraArgsRequired) {
            _extraArgs.append(timeOfOccurence).append(",").append(serviceTime).append(",").append(category).append(endl);
        }
    }

    public String getExtraArgs() {
        return _extraArgs.toString();
    }

    public void setEndTime(double endTime) {
        _endTime = endTime;
    }

    public double getAvgQueueTimePerCat(int category) {
        return _avgQueueTimePerCat[category].getAvgQueueingTime();
    }

    public double getAvgServiceTimePerCat(int category) {
        return _avgQueueTimePerCat[category].getAvgServiceTime();
    }

    public int getSimulatedJobsPerCat(int category) {
        return _avgQueueTimePerCat[category]._simulatedJobs;
    }

    public double getAvgQueueTime() {
        double sumOfTotalQueueingTime = 0.0;
        for (int category = 0; category < H; category++) {
            sumOfTotalQueueingTime += getAvgQueueTimePerCat(category);
        }
        return sumOfTotalQueueingTime / H;
    }

    public double getEndTime() {
        return _endTime;
    }

    public double getSumQueueTime() {
        double sumOfTotalQueueingTime = 0.0;
        for (int category = 0; category < H; category++) {
            sumOfTotalQueueingTime += _avgQueueTimePerCat[category]._sumOfQueueingTime;
        }
        return sumOfTotalQueueingTime;
    }

    @Override
    public String toString() {
        String result = _endTime + endl + getAvgQueueTime() + endl;
        for (int category = 0; category < H; category++) {
            result += _avgQueueTimePerCat[category];
        }
        return result;
    }

    class CategoryData {
        int _simulatedJobs;
        double _sumOfQueueingTime;
        double _sumOfServiceTime;

        public double getAvgQueueingTime() {
            if (_simulatedJobs == 0) {
                return 0.0;
            } else {
                return _sumOfQueueingTime / _simulatedJobs;
            }
        }
        
        public double getAvgServiceTime() {
            if (_simulatedJobs == 0) {
                return 0.0;
            } else {
                return _sumOfServiceTime / _simulatedJobs;
            }
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s%s", Double.toString(_simulatedJobs), Double.toString(this.getAvgQueueingTime()), Double.toString(this.getAvgServiceTime()), endl);
        }
    }

}
