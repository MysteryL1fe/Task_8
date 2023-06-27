package khanin.dmitrii.graph.logic;

public class Path {
    private Route route;
    private Transport transport;
    private Stop from, to;
    private int startTime, endTime;

    public Path(Route route, Transport transport, Stop from, Stop to, int startTime, int endTime) {
        this.route = route;
        this.transport = transport;
        this.from = from;
        this.to = to;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Stop getFrom() {
        return from;
    }

    public void setFrom(Stop from) {
        this.from = from;
    }

    public Stop getTo() {
        return to;
    }

    public void setTo(Stop to) {
        this.to = to;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
