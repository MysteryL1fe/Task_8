package khanin.dmitrii.graph.logic;

import khanin.dmitrii.graph.exceptions.MultipleUseOfOneStopException;
import khanin.dmitrii.graph.exceptions.RoadWithWrongLengthException;
import khanin.dmitrii.graph.exceptions.RoadWithoutStopException;
import khanin.dmitrii.graph.exceptions.RouteWithoutStopException;
import khanin.dmitrii.graph.graphs.AdjMatrixGraph;
import khanin.dmitrii.graph.graphs.WeightedGraph;

import java.util.*;

public class CityGraph extends WeightedGraph {
    private final Map<Stop, Integer> stopToIndex = new HashMap<>();
    private final Map<Integer, Stop> indexToStop = new HashMap<>();

    public CityGraph(ArrayList<Stop> stops, ArrayList<Road> roads,
                     ArrayList<Route> routes, ArrayList<Transport> transports)
            throws MultipleUseOfOneStopException, RouteWithoutStopException, RoadWithoutStopException,
            RoadWithWrongLengthException {
        super(stops.size());

        int index = 0;
        for (Stop stop : stops) {
            if (stopToIndex.containsKey(stop)) {
                throw new MultipleUseOfOneStopException("Множественное использование одной остановки");
            }
            stopToIndex.put(stop, index);
            indexToStop.put(index++, stop);
        }

        for (Road road : roads) {
            if (road.getFirstStop() == null || road.getSecondStop() == null) {
                throw new RoadWithoutStopException("Найдена дорога которая не соединяет остановки");
            }
            if (road.getLength() < 1) {
                throw new RoadWithWrongLengthException("Найдена дорога с нулевой или отрицательной длинной");
            }
            addAdge(stopToIndex.get(road.getFirstStop()), stopToIndex.get(road.getSecondStop()), road.getLength());
        }

        for (Route route : routes) {
            if (route.getRoadsList().size() == 0) {
                throw new RouteWithoutStopException("Найден маршрут без остановок");
            }
        }
    }

    public String getStopNameByIndex(int index) {
        return indexToStop.get(index).getName();
    }
}
