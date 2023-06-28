package khanin.dmitrii.graph.exceptions.transport;

public class NegativeTransportCostException extends Exception {
    public NegativeTransportCostException(String message) {
        super(message);
    }
}
