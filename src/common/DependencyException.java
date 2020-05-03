package common;

public class DependencyException extends Exception {

    public DependencyException(Exception cause) {
        super(cause);
    }

    public DependencyException(String message) {
        super(message);
    }
}
