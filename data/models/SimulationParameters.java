package data.models;

import java.util.List;
import java.util.Map;

public class SimulationParameters {
    private Map<String, Integer> _param;
    private List<Category> _categories;

    public SimulationParameters(Map<String, Integer> param, List<Category> categories) {
        this._param = param;
        this._categories = categories;
    }

    public Map<String, Integer> getParam() {return _param;}
    public List<Category> getCategories() {return _categories;}

    @Override
    public String toString() {
        String endl = System.lineSeparator();
        return String.format("SimulationParameters{%s  param=%s,%s  categories=%s%s}", endl, _param, endl, _categories, endl);
    }
}