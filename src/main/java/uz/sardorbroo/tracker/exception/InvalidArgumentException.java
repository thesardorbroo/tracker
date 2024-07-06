package uz.sardorbroo.tracker.exception;

public class InvalidArgumentException extends TrackerException {
    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String message, boolean trace) {
        super(message, trace);
    }
}
