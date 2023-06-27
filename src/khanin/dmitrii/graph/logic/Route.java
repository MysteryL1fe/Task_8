package khanin.dmitrii.graph.logic;

import java.util.ArrayList;

public class Route {
    private ArrayList<Road> roadsList;
    private ArrayList<Integer> stopTimesList;
    private String num;

    public Route(ArrayList<Road> roadsList, ArrayList<Integer> stopTimesList, String num) {
        this.roadsList = roadsList;
        this.stopTimesList = stopTimesList;
        this.num = num;
    }

    public ArrayList<Road> getRoadsList() {
        return (ArrayList<Road>) roadsList.clone();
    }

    public void setRoadsList(ArrayList<Road> roadsList) {
        this.roadsList = roadsList;
    }

    public ArrayList<Integer> getStopTimesList() {
        return (ArrayList<Integer>) stopTimesList.clone();
    }

    public void setStopTimesList(ArrayList<Integer> stopTimesList) {
        this.stopTimesList = stopTimesList;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
