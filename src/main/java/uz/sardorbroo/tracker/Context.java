package uz.sardorbroo.tracker;

import uz.sardorbroo.tracker.domain.Tracker;
import uz.sardorbroo.tracker.domain.TrackerConfig;
import uz.sardorbroo.tracker.domain.Verification;

import java.util.List;
import java.util.UUID;

public interface Context {

    boolean add(TrackerConfig config);

    boolean add(Tracker tracker);

    boolean add(Verification verification);

    List<Verification> getAllVerificationByTrackerId(UUID trackerId);
}
