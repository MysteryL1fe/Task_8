package khanin.dmitrii.graph.graphs;

import java.util.Arrays;
import java.util.Iterator;

public class WeightedGraph implements Graph {
    protected int[][] weightMatrix;
    protected int vCnt = 0, eCnt = 0;

    public WeightedGraph() {
        this(0);
    }

    public WeightedGraph(int vertexCount) {
        weightMatrix = new int[vertexCount][vertexCount];
        this.vCnt = vertexCount;
    }

    @Override
    public int vertexCount() {
        return vCnt;
    }

    @Override
    public int edgeCount() {
        return eCnt;
    }

    @Override
    public void addAdge(int v1, int v2) {
        throw new RuntimeException("Not supported");
    }

    public void addAdge(int v1, int v2, int weight) {
        int maxV = Math.max(v1, v2);
        if (maxV >= vertexCount()) {
            weightMatrix = Arrays.copyOf(weightMatrix, maxV + 1);
            for (int i = 0; i <= maxV; i++) {
                weightMatrix[i] = i < vCnt ? Arrays.copyOf(weightMatrix[i], maxV + 1) : new int[maxV + 1];
            }
            vCnt = maxV + 1;
        }
        if (weightMatrix[v1][v2] == 0) {
            eCnt++;
        }
        weightMatrix[v1][v2] = weight;
        weightMatrix[v2][v1] = weight;
    }

    @Override
    public void removeAdge(int v1, int v2) {
        if (weightMatrix[v1][v2] != 0) {
            weightMatrix[v1][v2] = 0;
            eCnt--;
        }
    }

    @Override
    public Iterable<Integer> adjacencies(int v) {
        return new Iterable<Integer>() {
            Integer nextAdj = null;

            @Override
            public Iterator<Integer> iterator() {
                for (int i = 0; i < vCnt; i++) {
                    if (weightMatrix[v][i] != 0) {
                        nextAdj = i;
                        break;
                    }
                }

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return nextAdj != null;
                    }

                    @Override
                    public Integer next() {
                        Integer result = nextAdj;
                        nextAdj = null;
                        for (int i = result + 1; i < vCnt; i++) {
                            if (weightMatrix[v][i] != 0) {
                                nextAdj = i;
                                break;
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }

    @Override
    public boolean isAdj(int v1, int v2) {
        return weightMatrix[v1][v2] != 0;
    }
}
