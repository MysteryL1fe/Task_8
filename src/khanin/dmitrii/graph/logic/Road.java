package khanin.dmitrii.graph.logic;

public class Road {
    private Stop firstStop, secondStop;
    private int length;

    public Road(Stop firstStop, Stop secondStop, int length) {
        this.firstStop = firstStop;
        this.secondStop = secondStop;
        this.length = length;
    }

    public Stop getFirstStop() {
        return firstStop;
    }

    public void setFirstStop(Stop firstStop) {
        this.firstStop = firstStop;
    }

    public Stop getSecondStop() {
        return secondStop;
    }

    public void setSecondStop(Stop secondStop) {
        this.secondStop = secondStop;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
