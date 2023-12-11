package simulation.models;

import java.util.ArrayList;
import java.util.List;

import data.models.SimulationParameters;

public class SimulationsData {

    List<SimulationData> simulationsData;
    private int H;
    private int N;

    public SimulationsData(SimulationParameters params) {
        simulationsData = new ArrayList<SimulationData>();
        H = params.getParam().get("H");
        N = params.getParam().get("N");
    }

    public void addData(SimulationData simulationData) {
        simulationsData.add(simulationData);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        double totalEndTime = 0;
        double totalAvgQueueTime = 0;

        for (int i = 0; i < simulationsData.size(); i++) {
            totalEndTime += simulationsData.get(i).getEndTime();
            totalAvgQueueTime += simulationsData.get(i).getAvgQueueTime();
        }

        double avgEndTime = totalEndTime / simulationsData.size();
        double avgQueueTime = totalAvgQueueTime / simulationsData.size();

        result.append(avgEndTime).append("\n");
        result.append(avgQueueTime).append("\n");

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
            result.append(avgCategoryServiceTime).append("\n");
        }

        return result.toString();
    }
    
}
