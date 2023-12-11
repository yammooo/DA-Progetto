package simulation.models;

import data.models.SimulationParameters;

public class SimulationData {

    private SimulationParameters params;
    private double _endTime;
    private CategoryData[] _avgQueueTimePerCat;
    private int H;

    public SimulationData(SimulationParameters params) {
        this.params = params;
        H = params.getParam().get("H");

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

    @Override
    public String toString() {
        String endl = System.lineSeparator();
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
            String endl = System.lineSeparator();
            return String.format("%s,%s,%s%s", Double.toString(_simulatedJobs), Double.toString(this.getAvgQueueingTime()), Double.toString(this.getAvgServiceTime()), endl);
        }
    }

}
