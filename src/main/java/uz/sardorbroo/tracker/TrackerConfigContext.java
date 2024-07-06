package uz.sardorbroo.tracker;

import uz.sardorbroo.tracker.domain.Tracker;
import uz.sardorbroo.tracker.domain.TrackerConfig;
import uz.sardorbroo.tracker.domain.Verification;

import java.util.*;

// todo make it more abstract
public class TrackerConfigContext {
    private static final ClassLoader CLASS_LOADER = ClassLoader.getSystemClassLoader();
    private static final TrackerConfigContext context = new TrackerConfigContext();
    private final Map<Class<? extends Object>, TrackerConfig> configs = new HashMap<>();
    private final Map<UUID, Tracker> trackers = new HashMap<>();
    private final Map<UUID, Verification> verifications = new HashMap<>();

    private TrackerConfigContext() {
    }

    public static TrackerConfigContext getInstance() {
        return context;
    }

    public boolean add(TrackerConfig config) throws ClassNotFoundException {

        if (Objects.isNull(config)) {
            throw new NullPointerException("Invalid argument is passed! TrackerConfig must not be null!");
        }

        String entity = config.getEntity();
        Class<? extends Object> clazz = CLASS_LOADER.loadClass(entity);

        configs.put(clazz, config);
        return true;
    }

    public boolean add(Tracker tracker) {

        if (Objects.isNull(tracker)) {
            throw new RuntimeException("Invalid argument is passed! Tracker must not be null!");
        }

        UUID id = tracker.getId();
        trackers.put(id, tracker);
        return true;
    }

    public boolean add(Verification verification) {

        if (Objects.isNull(verification)) {
            throw new RuntimeException("Invalid argument is passed! Verification must not be null!");
        }

        UUID id = verification.getId();
        verifications.put(id, verification);
        return true;
    }

    public List<Verification> getAllVerificationsByTrackerId(UUID trackerId) {
        return verifications.values().stream()
                .filter(verification -> Objects.equals(trackerId, verification.getTrackerId()))
                .toList();
    }

    public TrackerConfig get(Class<? extends Object> clazz) {
        return configs.get(clazz);
    }

    public Tracker get(UUID id) {
        return trackers.get(id);
    }

    public TrackerConfig get(String entity) throws ClassNotFoundException {
        return get(Class.forName(entity));
    }

    public List<TrackerConfig> getAllConfigs() {
        return configs.values().stream().toList();
    }

    public List<Tracker> getAllTrackers() {
        return trackers.values().stream().toList();
    }

    public List<Verification> getAllVerifications() {
        return verifications.values().stream().toList();
    }
}
