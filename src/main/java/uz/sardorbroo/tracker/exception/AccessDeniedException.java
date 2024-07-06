package uz.sardorbroo.tracker.exception;

public class AccessDeniedException extends TrackerException {
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, boolean trace) {
        super(message, trace);
    }
}
