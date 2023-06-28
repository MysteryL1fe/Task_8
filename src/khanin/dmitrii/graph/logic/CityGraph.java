package khanin.dmitrii.graph.logic;

import khanin.dmitrii.graph.exceptions.road.*;
import khanin.dmitrii.graph.exceptions.route.*;
import khanin.dmitrii.graph.exceptions.stop.*;
import khanin.dmitrii.graph.exceptions.transport.*;
import khanin.dmitrii.graph.graphs.WeightedGraph;

import java.util.*;

public class CityGraph extends WeightedGraph {
    private final Map<Stop, Integer> stopToIndex = new HashMap<>();
    private final Map<Integer, Stop> indexToStop = new HashMap<>();
    private final Map<Route, Integer> routeToIndex = new HashMap<>();
    private final Map<Integer, Route> indexToRoute = new HashMap<>();
    private final ArrayList<ArrayList<Stop>> routesStops = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> routesDelayTimes = new ArrayList<>();

    public CityGraph(ArrayList<Stop> stops, ArrayList<Road> roads,
                     ArrayList<Route> routes, ArrayList<Transport> transports)
            throws MultipleUseOfOneStopException, RouteWithoutStopException, RoadWithoutStopException,
            WrongRoadLengthException, EmptyStopNameException, EmptyRouteDelayTimesException,
            DifferentRouteStopsAndDelayTimesCountsException, RoadConnectSameStopsException, EmptyRouteNumException,
            NegativeTransportStartTimeException, NegativeTransportCostException, EmptyTransportNumException,
            EmptyTransportRouteException, NegativeTransportSpeedException, RouteWithRoadBreakException {

        super(stops.size());

        int index = 0;
        for (Stop stop : stops) {
            if (stop.getName().isEmpty()) {
                throw new EmptyStopNameException("Найдена остановка без имени");
            }
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
            if (road.getFirstStop().equals(road.getSecondStop())) {
                throw new RoadConnectSameStopsException("Найдена дорога, соединяющая одну и ту же остановку");
            }
            if (road.getLength() < 1) {
                throw new WrongRoadLengthException("Найдена дорога с нулевой или отрицательной длинной");
            }
            int firstIndex = stopToIndex.get(road.getFirstStop()), secondIndex = stopToIndex.get(road.getSecondStop());
            if (!(weightMatrix[firstIndex][secondIndex] > 0)
                    || road.getLength() < weightMatrix[firstIndex][secondIndex]) {
                addAdge(firstIndex, secondIndex, road.getLength());
            }
        }

        for (Route route : routes) {
            if (route.getRoadsList().size() == 0) {
                throw new RouteWithoutStopException("Найден маршрут без остановок");
            }
            if (route.getDelayTimesList().size() == 0) {
                throw new EmptyRouteDelayTimesException(
                        "Найден маршрут, для которого не указано время ожидания на остановке"
                );
            }
            if (route.getRoadsList().size() + 1 != route.getDelayTimesList().size()) {
                throw new DifferentRouteStopsAndDelayTimesCountsException(
                        "Найден маршрут, у которого не совпадают кол-во остановок и кол-во времени ожидания на них"
                );
            }
            if (route.getNum().isEmpty()) {
                throw new EmptyRouteNumException("Найден маршрут без номера");
            }

            ArrayList<Stop> fullRouteStops = new ArrayList<>();
            Set<Stop> visitedStops = new HashSet<>();

            Iterator<Road> it1 = route.getRoadsList().iterator();
            Iterator<Road> it2 = route.getRoadsList().iterator();
            Road road = it2.next();
            if (road.getFirstStop().equals(road.getSecondStop())) {
                throw new RoadConnectSameStopsException("Найдена дорога, соединяющая одну и ту же остановку");
            }
            fullRouteStops.add(road.getFirstStop());
            visitedStops.add(road.getFirstStop());
            while (it2.hasNext()) {
                Road road1 = it1.next();
                Road road2 = it2.next();
                if (road1.getSecondStop() != road2.getFirstStop()) {
                    throw new RouteWithRoadBreakException("Найден маршрут с разрывом в пути");
                }
                if (road2.getFirstStop().equals(road2.getSecondStop())) {
                    throw new RoadConnectSameStopsException("Найдена дорога, соединяющая одну и ту же остановку");
                }
                fullRouteStops.add(road2.getFirstStop());
                visitedStops.add(road2.getFirstStop());
            }

            road = it1.next();
            fullRouteStops.add(road.getSecondStop());
            ArrayList<Integer> fullRouteTimeDelays = route.getDelayTimesList();
            if (visitedStops.contains(road.getSecondStop())) {
                int i = fullRouteStops.indexOf(road.getSecondStop());
                List<Road> tmp = route.getRoadsList().subList(0, i);
                Collections.reverse(tmp);
                for (Road road1 : tmp) {
                    fullRouteStops.add(road1.getFirstStop());
                }
                List<Integer> tmp2 = route.getDelayTimesList();
                tmp2 = tmp2.subList(0, i);
                Collections.reverse(tmp2);
                fullRouteTimeDelays.addAll(tmp2);
            } else {
                ArrayList<Road> tmp = route.getRoadsList();
                Collections.reverse(tmp);
                for (Road road1 : tmp) {
                    fullRouteStops.add(road1.getFirstStop());
                }
                List<Integer> tmp2 = route.getDelayTimesList();
                tmp2 = tmp2.subList(0, tmp2.size() - 1);
                Collections.reverse(tmp2);
                fullRouteTimeDelays.addAll(tmp2);
            }
            routeToIndex.put(route, routesStops.size());
            indexToRoute.put(routesStops.size(), route);
            routesStops.add(fullRouteStops);
            routesDelayTimes.add(fullRouteTimeDelays);
        }

        for (Transport transport : transports) {
            if (transport.getStartTime() < 0) {
                throw new NegativeTransportStartTimeException(
                        "Найден транспорт с отрицательным временем начала движения"
                );
            }
            if (transport.getCost() < 0) {
                throw new NegativeTransportCostException(
                        "Найден транспорт с отрицательной стоимостью за единицу расстояния"
                );
            }
            if (transport.getNum().isEmpty()) {
                throw new EmptyTransportNumException("Найден транспорт без номера");
            }
            if (transport.getRoute() == null) {
                throw new EmptyTransportRouteException("Найден транспорт без маршрута");
            }
            if (transport.getSpeed() < 1) {
                throw new NegativeTransportSpeedException("Найден транспорт с нулевой или отрицательной скоростью");
            }
        }
    }

    public String getStopNameByIndex(int index) {
        return indexToStop.get(index).getName();
    }
}
