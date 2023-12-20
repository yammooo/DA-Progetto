package simulation.models;

import java.util.ArrayList;
import java.util.List;

import data.models.SimulationParameters;

public class SimulationsData {

    List<SimulationData> simulationsData;
    private String endl = System.lineSeparator();
    private boolean _areExtraArgsRequired;
    private int K;
    private int H;
    private int N;
    private int R;
    private int P;

    public SimulationsData(SimulationParameters params) {
        simulationsData = new ArrayList<SimulationData>();
        K = params.getParam().get("K");
        H = params.getParam().get("H");
        N = params.getParam().get("N");
        R = params.getParam().get("R");
        P = params.getParam().get("P");

        _areExtraArgsRequired = (R == 1 && N <= 10 && P == 0);
    }

    public void addSimulationData(SimulationData simulationData) {
        simulationsData.add(simulationData);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(K).append(",");
        result.append(H).append(",");
        result.append(N).append(",");
        result.append(R).append(",");
        result.append(P).append(endl);

        if(_areExtraArgsRequired){
            result.append(simulationsData.get(0).getExtraArgs());
        }

        double totalEndTime = 0;
        double totalAvgQueueTime = 0;

        for (int i = 0; i < simulationsData.size(); i++) {
            totalEndTime += simulationsData.get(i).getEndTime();
            totalAvgQueueTime += simulationsData.get(i).getSumQueueTime();
        }

        double avgEndTime = totalEndTime / simulationsData.size();
        double avgQueueTime = totalAvgQueueTime / (simulationsData.size() * N);

        result.append(avgEndTime).append(endl);
        result.append(avgQueueTime).append(endl);

        for (int category = 0; category < H; category++) {
            double totalCategoryQueueTime = 0;
            double totalCategoryServiceTime = 0;
            int totalSimulatedJobs = 0;

            for (int i = 0; i < simulationsData.size(); i++) {
                totalSimulatedJobs += simulationsData.get(i).getSimulatedJobsPerCat(category);
                totalCategoryQueueTime += simulationsData.get(i).getAvgQueueTimePerCat(category);
                totalCategoryServiceTime += simulationsData.get(i).getAvgServiceTimePerCat(category);
            }

            double avgCategoryQueueTime = totalCategoryQueueTime / simulationsData.size();
            double avgCategoryServiceTime = totalCategoryServiceTime / simulationsData.size();

            result.append((double) totalSimulatedJobs / simulationsData.size()).append(",");
            result.append(avgCategoryQueueTime).append(",");
            result.append(avgCategoryServiceTime).append(endl);
        }

        return result.toString();
    }
    
}
