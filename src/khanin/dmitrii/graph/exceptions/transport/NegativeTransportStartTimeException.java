package khanin.dmitrii.graph.exceptions.transport;

public class NegativeTransportStartTimeException extends Exception {
    public NegativeTransportStartTimeException(String message) {
        super(message);
    }
}
