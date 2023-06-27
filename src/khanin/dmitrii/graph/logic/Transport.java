package khanin.dmitrii.graph.logic;

public class Transport {
    private Route route;
    private int startTime, speed, cost, num;

    public Transport(Route route, int startTime, int speed, int cost, int num) {
        this.route = route;
        this.startTime = startTime;
        this.speed = speed;
        this.cost = cost;
        this.num = num;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
