package uz.sardorbroo.tracker.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackerException extends RuntimeException {

    private boolean trace = false;

    public TrackerException(String message) {
        super(message);
    }

    public TrackerException(String message, boolean trace) {
        super(message);
        this.trace = trace;
    }

    @Override
    public String getMessage() {
        if (trace) {
            log.warn(super.getMessage(), this);
        } else {
            log.warn(super.getMessage());
        }
        return super.getMessage();
    }
}
