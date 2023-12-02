package data.handlers;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import data.models.Category;
import data.models.SimulationParameters;

public class SimulationDataHandler{

    public static SimulationParameters readFileData(String fileName){
        Map<String, Integer> param = new HashMap<String, Integer>();
        List<Category> categories = new ArrayList<Category>();

        return new SimulationParameters(param, categories);
    }

}