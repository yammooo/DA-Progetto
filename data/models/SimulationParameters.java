package data.models;

import java.util.List;
import java.util.Map;

public class SimulationParameters {
    private Map<String, Integer> _param;
    private List<ExpDistGenerator> _expDistGenerators;

    public SimulationParameters(Map<String, Integer> param, List<ExpDistGenerator> expDistGenerators) {
        _param = param;
        _expDistGenerators = expDistGenerators;
    }

    public Map<String, Integer> getParam() {return _param;}
    public List<ExpDistGenerator> getCategories() {return _expDistGenerators;}

    @Override
    public String toString() {
        String endl = System.lineSeparator();
        return String.format("SimulationParameters{%s  param=%s,%s  expDistGenerators=%s%s}", endl, _param, endl, _expDistGenerators, endl);
    }
}