package khanin.dmitrii.graph.logic;

import java.util.ArrayList;

public class Route {
    private ArrayList<Road> roadsList;
    private ArrayList<Integer> delayTimesList;
    private String num;

    public Route(ArrayList<Road> roadsList, ArrayList<Integer> stopTimesList, String num) {
        this.roadsList = roadsList;
        this.delayTimesList = stopTimesList;
        this.num = num;
    }

    public ArrayList<Road> getRoadsList() {
        return (ArrayList<Road>) roadsList.clone();
    }

    public void setRoadsList(ArrayList<Road> roadsList) {
        this.roadsList = roadsList;
    }

    public ArrayList<Integer> getDelayTimesList() {
        return (ArrayList<Integer>) delayTimesList.clone();
    }

    public void setDelayTimesList(ArrayList<Integer> delayTimesList) {
        this.delayTimesList = delayTimesList;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
