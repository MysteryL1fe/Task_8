package khanin.dmitrii.graph.graphs;

import khanin.dmitrii.graph.logic.CityGraph;

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
                        "  %s %s %s", graph.getStopNameByIndex(v1), "--", graph.getStopNameByIndex(v2)
                )).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(graph.getStopNameByIndex(v1)).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
}
