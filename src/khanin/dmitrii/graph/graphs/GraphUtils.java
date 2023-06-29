package khanin.dmitrii.graph.graphs;

import khanin.dmitrii.graph.logic.CityGraph;
import khanin.dmitrii.graph.logic.Path;
import khanin.dmitrii.graph.logic.Road;
import khanin.dmitrii.graph.logic.Route;

import java.util.ArrayList;

/**
 * Утилиты работы с графами
 */
public class GraphUtils {
    /**
     * Получение dot-описяния графа (для GraphViz)
     * @return
     */
    public static String toDot(Graph graph) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        sb.append("strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < graph.vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : graph.adjacencies(v1)) {
                sb.append(String.format("  %d %s %d", v1, "--", v2)).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }

    public static String toDot(CityGraph graph) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        sb.append("strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < graph.vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : graph.adjacencies(v1)) {
                sb.append(String.format(
                        "  \"%s\" %s \"%s\" [label=\"%s\"]", graph.getStopNameByIndex(v1), "--",
                        graph.getStopNameByIndex(v2), graph.getWeight(v1, v2)
                )).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(String.format("\"%s\"", graph.getStopNameByIndex(v1))).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }

    public static String toDot(CityGraph graph, Route route) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        sb.append("strict graph").append(" {").append(nl);
        if (route != null) {
            for (Road road : route.getRoadsList()) {
                sb.append(String.format(
                        "  \"%s\" %s \"%s\" [label=\"%s\", color=red]",
                        road.getFirstStop().getName(), "--", road.getSecondStop().getName(), road.getLength()
                )).append(nl);
            }
        }
        for (int v1 = 0; v1 < graph.vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : graph.adjacencies(v1)) {
                sb.append(String.format(
                        "  \"%s\" %s \"%s\" [label=\"%s\"]", graph.getStopNameByIndex(v1), "--",
                        graph.getStopNameByIndex(v2), graph.getWeight(v1, v2)
                )).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(String.format("\"%s\"", graph.getStopNameByIndex(v1))).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }

    public static String toDot(CityGraph graph, ArrayList<Path> paths) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        sb.append("strict graph").append(" {").append(nl);
        if (paths != null) {
            for (Path path : paths) {
                sb.append(String.format(
                        "  \"%s\" %s \"%s\" [label=\"№%s\", color=green]",
                        path.getFrom().getName(), "--", path.getTo().getName(), path.getTransport().getNum()
                )).append(nl);
            }
        }
        for (int v1 = 0; v1 < graph.vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : graph.adjacencies(v1)) {
                sb.append(String.format(
                        "  \"%s\" %s \"%s\"", graph.getStopNameByIndex(v1), "--", graph.getStopNameByIndex(v2)
                )).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(String.format("\"%s\"", graph.getStopNameByIndex(v1))).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
}
