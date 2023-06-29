package khanin.dmitrii.graph.logic;

import khanin.dmitrii.graph.exceptions.StopIsNotExist;
import khanin.dmitrii.graph.exceptions.StopIsNotSpecified;
import khanin.dmitrii.graph.exceptions.road.*;
import khanin.dmitrii.graph.exceptions.route.*;
import khanin.dmitrii.graph.exceptions.stop.*;
import khanin.dmitrii.graph.exceptions.transport.*;
import khanin.dmitrii.graph.graphs.WeightedGraph;

import java.util.*;

public class CityGraph extends WeightedGraph {
    private final Map<Stop, Integer> stopToIndex = new HashMap<>();
    private final Map<Integer, Stop> indexToStop = new HashMap<>();
    private final ArrayList<ArrayList<Stop>> routesStops = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> routesDelayTimes = new ArrayList<>();
    private final HashMap<Stop, ArrayList<ExtendedTransport>> transportsToStop = new HashMap<>();

    public CityGraph(ArrayList<Stop> stops, ArrayList<Road> roads,
                     ArrayList<Route> routes, ArrayList<Transport> transports)
            throws MultipleUseOfOneStopException, RouteWithoutStopException, RoadWithoutStopException,
            WrongRoadLengthException, EmptyStopNameException, EmptyRouteDelayTimesException,
            DifferentRouteStopsAndDelayTimesCountsException, RoadConnectSameStopsException, EmptyRouteNumException,
            NegativeTransportStartTimeException, NegativeTransportCostException, EmptyTransportNumException,
            EmptyTransportRouteException, NegativeTransportSpeedException, RouteWithRoadBreakException,
            WrongTransportRouteException, NegativeRouteDelayTimeException {

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

        Map<Route, Integer> routeToIndex = new HashMap<>();
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

            for (int i : route.getDelayTimesList()) {
                if (i < 0) {
                    throw new NegativeRouteDelayTimeException("Найден маршрут с отрицательным временем остановки");
                }
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
            if (!routeToIndex.containsKey(transport.getRoute())) {
                throw new WrongTransportRouteException("Найден транспорт с неизвестным маршрутом");
            }
            new ExtendedTransport(transport, routeToIndex.get(transport.getRoute()));
        }
    }

    public String getStopNameByIndex(int index) {
        return indexToStop.get(index).getName();
    }

    public ArrayList<Path> findShortestPath(Stop from, Stop to) throws StopIsNotSpecified, StopIsNotExist {
        if (from == null || to == null) throw new StopIsNotSpecified("Не указано от/до какой остановки");
        if (!stopToIndex.containsKey(from) || !stopToIndex.containsKey(to)) {
            throw new StopIsNotExist("Указанной остановки не найдено");
        }

        ArrayList<Path> result = new ArrayList<>();

        int n = stopToIndex.size();
        int fromIndex = stopToIndex.get(from);
        int toIndex = stopToIndex.get(to);
        int[] d = new int[n];
        int[] fromStop = new int[n];
        boolean[] found = new boolean[n];

        Arrays.fill(d, Integer.MAX_VALUE);
        d[fromIndex] = 0;
        Arrays.fill(fromStop, -1);

        for (int i = 0; i < n; i++) {
            int cur = -1;
            for (int j = 0; j < n; j++) {
                if (!found[j] && (cur < 0 || d[j] < d[cur])) {
                    cur = j;
                }
            }

            found[cur] = true;
            if (cur == toIndex) {
                break;
            }
            ArrayList<ExtendedTransport> transportsCur = transportsToStop.get(indexToStop.get(cur));
            for (int next = 0; next < n; next++) {
                ArrayList<ExtendedTransport> transports = new ArrayList<>();
                for (ExtendedTransport transport : transportsToStop.get(indexToStop.get(next))) {
                    if (transportsCur.contains(transport)) {
                        transports.add(transport);
                    }
                }
                ArrayList<ExtendedTransport> suitableTransports = new ArrayList<>();
                for (ExtendedTransport transport : transports) {
                    Iterator<Stop> it1 = routesStops.get(transport.routeIndex).iterator();
                    Iterator<Stop> it2 = routesStops.get(transport.routeIndex).iterator();
                    it2.next();
                    while (it2.hasNext()) {
                        Stop stop1 = it1.next();
                        Stop stop2 = it2.next();
                        if (stopToIndex.get(stop1) == cur && stopToIndex.get(stop2) == next) {
                            suitableTransports.add(transport);
                            break;
                        }
                    }
                }
                if (suitableTransports.size() > 0) {
                    if (weightMatrix[cur][next] != 0 && d[cur] + weightMatrix[cur][next] < d[next]) {
                        d[next] = d[cur] + weightMatrix[cur][next];
                        fromStop[next] = cur;
                    }
                }
            }
        }

        int pathFrom = fromStop[toIndex], pathTo = toIndex;
        ArrayList<Integer> path = new ArrayList<>();
        path.add(pathTo);
        while (pathFrom >= 0 && d[pathTo] < Integer.MAX_VALUE) {
            path.add(pathFrom);
            pathTo = pathFrom;
            pathFrom = fromStop[pathFrom];
        }

        Collections.reverse(path);
        if (path.get(0) != fromIndex) return result;

        int time = 0;
        Iterator<Integer> pathIt1 = path.iterator();
        Iterator<Integer> pathIt2 = path.iterator();
        pathIt2.next();
        while (pathIt2.hasNext()) {
            int cur = pathIt1.next(), next = pathIt2.next();
            ArrayList<ExtendedTransport> transportsCur = transportsToStop.get(indexToStop.get(cur));
            ArrayList<ExtendedTransport> transports = new ArrayList<>();
            for (ExtendedTransport transport : transportsToStop.get(indexToStop.get(next))) {
                if (transportsCur.contains(transport)) {
                    transports.add(transport);
                }
            }
            int minStartTime = 0;
            int minEndTime = Integer.MAX_VALUE;
            int minCost = 0;
            Transport minTransport = null;
            for (ExtendedTransport transport : transports) {
                int i = 0;
                Iterator<Stop> it1 = routesStops.get(transport.routeIndex).iterator();
                Iterator<Stop> it2 = routesStops.get(transport.routeIndex).iterator();
                it2.next();
                while (it2.hasNext()) {
                    Stop stop1 = it1.next();
                    Stop stop2 = it2.next();
                    if (stopToIndex.get(stop1) == cur && stopToIndex.get(stop2) == next) {
                        int j = 0;
                        while (transport.transport.getStartTime() + transport.timeFromStartToStop.get(i)
                                + transport.delayTimesOnStop.get(i) + j * transport.routeTime < time) {
                            j++;
                        }
                        int curStartTime = Math.max(
                                time, transport.transport.getStartTime() + transport.timeFromStartToStop.get(i)
                                        + j * transport.routeTime
                        );
                        int curEndTime = transport.transport.getStartTime() + transport.timeFromStartToStop.get(i + 1)
                                    + j * transport.routeTime;
                        int curCost = weightMatrix[stopToIndex.get(stop1)][stopToIndex.get(stop2)]
                                * transport.transport.getCost();
                        if (curEndTime < minEndTime) {
                            minStartTime = curStartTime;
                            minEndTime = curEndTime;
                            minCost = curCost;
                            minTransport = transport.transport;
                        }
                    }
                    i++;
                }
            }
            result.add(new Path(
                    minTransport.getRoute(), minTransport, indexToStop.get(cur), indexToStop.get(next),
                    minStartTime, minEndTime, minCost
            ));
            time = minEndTime;
        }

        return result;
    }

    public ArrayList<Path> findCheapestPath(Stop from, Stop to) throws StopIsNotSpecified, StopIsNotExist {
        if (from == null || to == null) throw new StopIsNotSpecified("Не указано от/до какой остановки");
        if (!stopToIndex.containsKey(from) || !stopToIndex.containsKey(to)) {
            throw new StopIsNotExist("Указанной остановки не найдено");
        }

        ArrayList<Path> result = new ArrayList<>();

        int n = stopToIndex.size();
        int fromIndex = stopToIndex.get(from);
        int toIndex = stopToIndex.get(to);
        int[] d = new int[n];
        int[] fromStop = new int[n];
        boolean[] found = new boolean[n];

        Arrays.fill(d, Integer.MAX_VALUE);
        d[fromIndex] = 0;
        Arrays.fill(fromStop, -1);

        for (int i = 0; i < n; i++) {
            int cur = -1;
            for (int j = 0; j < n; j++) {
                if (!found[j] && (cur < 0 || d[j] < d[cur])) {
                    cur = j;
                }
            }

            found[cur] = true;
            if (cur == toIndex) {
                break;
            }
            ArrayList<ExtendedTransport> transportsCur = transportsToStop.get(indexToStop.get(cur));
            for (int next = 0; next < n; next++) {
                ArrayList<ExtendedTransport> transports = new ArrayList<>();
                for (ExtendedTransport transport : transportsToStop.get(indexToStop.get(next))) {
                    if (transportsCur.contains(transport)) {
                        transports.add(transport);
                    }
                }
                ArrayList<ExtendedTransport> suitableTransports = new ArrayList<>();
                for (ExtendedTransport transport : transports) {
                    Iterator<Stop> it1 = routesStops.get(transport.routeIndex).iterator();
                    Iterator<Stop> it2 = routesStops.get(transport.routeIndex).iterator();
                    it2.next();
                    while (it2.hasNext()) {
                        Stop stop1 = it1.next();
                        Stop stop2 = it2.next();
                        if (stopToIndex.get(stop1) == cur && stopToIndex.get(stop2) == next) {
                            suitableTransports.add(transport);
                            break;
                        }
                    }
                }
                if (suitableTransports.size() > 0) {
                    for (ExtendedTransport transport : suitableTransports) {
                        if (weightMatrix[cur][next] != 0
                                && d[cur] + weightMatrix[cur][next] * transport.transport.getCost() < d[next]) {
                            d[next] = d[cur] + weightMatrix[cur][next] * transport.transport.getCost();
                            fromStop[next] = cur;
                        }
                    }
                }
            }
        }

        int pathFrom = fromStop[toIndex], pathTo = toIndex;
        ArrayList<Integer> path = new ArrayList<>();
        path.add(pathTo);
        while (pathFrom >= 0 && d[pathTo] < Integer.MAX_VALUE) {
            path.add(pathFrom);
            pathTo = pathFrom;
            pathFrom = fromStop[pathFrom];
        }

        Collections.reverse(path);
        if (path.get(0) != fromIndex) return result;

        int time = 0;
        Iterator<Integer> pathIt1 = path.iterator();
        Iterator<Integer> pathIt2 = path.iterator();
        pathIt2.next();
        while (pathIt2.hasNext()) {
            int cur = pathIt1.next(), next = pathIt2.next();
            ArrayList<ExtendedTransport> transportsCur = transportsToStop.get(indexToStop.get(cur));
            ArrayList<ExtendedTransport> transports = new ArrayList<>();
            for (ExtendedTransport transport : transportsToStop.get(indexToStop.get(next))) {
                if (transportsCur.contains(transport)) {
                    transports.add(transport);
                }
            }
            int minStartTime = 0;
            int minEndTime = 0;
            int minCost = Integer.MAX_VALUE;
            Transport minTransport = null;
            for (ExtendedTransport transport : transports) {
                int i = 0;
                Iterator<Stop> it1 = routesStops.get(transport.routeIndex).iterator();
                Iterator<Stop> it2 = routesStops.get(transport.routeIndex).iterator();
                it2.next();
                while (it2.hasNext()) {
                    Stop stop1 = it1.next();
                    Stop stop2 = it2.next();
                    if (stopToIndex.get(stop1) == cur && stopToIndex.get(stop2) == next) {
                        int j = 0;
                        while (transport.transport.getStartTime() + transport.timeFromStartToStop.get(i)
                                + transport.delayTimesOnStop.get(i) + j * transport.routeTime < time) {
                            j++;
                        }
                        int curStartTime = Math.max(
                                time, transport.transport.getStartTime() + transport.timeFromStartToStop.get(i)
                                        + j * transport.routeTime
                        );
                        int curEndTime = transport.transport.getStartTime() + transport.timeFromStartToStop.get(i + 1)
                                + j * transport.routeTime;
                        int curCost = weightMatrix[stopToIndex.get(stop1)][stopToIndex.get(stop2)]
                                * transport.transport.getCost();
                        if (curCost < minCost || curCost == minCost && curEndTime < minEndTime) {
                            minStartTime = curStartTime;
                            minEndTime = curEndTime;
                            minCost = curCost;
                            minTransport = transport.transport;
                        }
                    }
                    i++;
                }
            }
            result.add(new Path(
                    minTransport.getRoute(), minTransport, indexToStop.get(cur), indexToStop.get(next),
                    minStartTime, minEndTime, minCost
            ));
            time = minEndTime;
        }

        return result;
    }

    private class ExtendedTransport {
        private final Transport transport;
        private final int routeIndex;
        private int routeTime = 0;
        private final ArrayList<Integer> timeFromStartToStop = new ArrayList<>();
        private final ArrayList<Integer> delayTimesOnStop = new ArrayList<>();

        public ExtendedTransport(Transport transport, int routeIndex) {
            this.transport = transport;
            this.routeIndex = routeIndex;

            Iterator<Stop> stopsIt = routesStops.get(routeIndex).iterator();
            Iterator<Integer> delayTimesIt = routesDelayTimes.get(routeIndex).iterator();
            Set<Stop> visitedStops = new HashSet<>();

            Stop lastStop = null;
            while (stopsIt.hasNext() && delayTimesIt.hasNext()) {
                Stop stop = stopsIt.next();
                Integer delayTime = delayTimesIt.next();

                if (lastStop != null) {
                    routeTime += (weightMatrix[stopToIndex.get(lastStop)][stopToIndex.get(stop)]
                            + transport.getSpeed() - 1) / transport.getSpeed();
                }

                timeFromStartToStop.add(routeTime);
                delayTimesOnStop.add(delayTime);

                routeTime += delayTime;

                if (visitedStops.add(stop)) {
                    if (transportsToStop.containsKey(stop)) {
                        transportsToStop.get(stop).add(this);
                    } else {
                        ArrayList<ExtendedTransport> tmp = new ArrayList<>();
                        tmp.add(this);
                        transportsToStop.put(stop, tmp);
                    }
                }

                lastStop = stop;
            }
        }
    }
}
