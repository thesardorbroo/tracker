package uz.sardorbroo.tracker.exception;

public class NotFoundException extends TrackerException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, boolean trace) {
        super(message, trace);
    }
}
