package uz.sardorbroo.tracker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import uz.sardorbroo.tracker.TrackerConfigContext;
import uz.sardorbroo.tracker.TrackerPredications;
import uz.sardorbroo.tracker.domain.Storage;
import uz.sardorbroo.tracker.domain.Tracker;
import uz.sardorbroo.tracker.domain.TrackerConfig;
import uz.sardorbroo.tracker.domain.Verification;
import uz.sardorbroo.tracker.domain.enumeration.TrackingType;
import uz.sardorbroo.tracker.pojo.Checking;
import uz.sardorbroo.tracker.pojo.VerificationResult;
import uz.sardorbroo.tracker.pojo.Verified;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TrackerProcessor<ENTITY, ID> {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private final TrackerConfigContext context = TrackerConfigContext.getInstance();
    private final Storage<ENTITY, ID> storage;
    private final TrackerPredications trackerPredications;

    public TrackerProcessor(Storage<ENTITY, ID> storage, TrackerPredications trackerPredications) {
        this.storage = storage;
        this.trackerPredications = trackerPredications;
    }

    public Tracker create(ENTITY entity, Supplier<String> userIdFinder) {
        return track(entity, userIdFinder.get(), TrackingType.CREATE);
    }

    public Tracker update(ENTITY entity, Supplier<String> userIdFinder) {
        return track(entity, userIdFinder.get(), TrackingType.UPDATE);
    }

    public Tracker delete(ENTITY entity, Supplier<String> userIdFinder) {
        return track(entity, userIdFinder.get(), TrackingType.DELETE);
    }

    public Tracker track(ENTITY entity, String userId, TrackingType type) {

        Tracker tracker = new Tracker()
                .setId(UUID.randomUUID())
                .setType(type)
                .setData(convertToString(entity))
                .setEntity(entity.getClass().getName())
                .setVerified(false)
                .setCreateBy(userId)
                .setCreateAt(Instant.now());

        context.add(tracker);
        return tracker;
    }

    @SneakyThrows
    // make dynamic getting verifier ID
    public VerificationResult verify(UUID trackerId, String verifierId) throws RuntimeException {

        Tracker tracker = context.get(trackerId);
        if (tracker.isVerified()) {
            throw new RuntimeException("Tracker has already verified!");
        }
        TrackerConfig config = context.get(tracker.getEntity());

        Predicate<String> predication = trackerPredications.getById(config.getPredicationId());
        if (!predication.test(verifierId)) {
            throw new RuntimeException("You cannot verify this data!");
        }

        // check is user really can verify
        Verification verification = new Verification()
                .setId(UUID.randomUUID())
                .setTrackerId(trackerId)
                .setCreatedBy(verifierId)
                .setCreatedAt(Instant.now());
        context.add(verification);

        List<Verification> verifications = context.getAllVerificationsByTrackerId(tracker.getId());
        if (verifications.size() == config.getVerificationCount()) {

            tracker.setVerified(true);
            context.add(tracker);

            ENTITY entity = convertToObj(tracker);
            Function<ENTITY, ENTITY> executor = storage.findMethodByTrackingType(tracker.getType());
            ENTITY appliedEntity = executor.apply(entity);

            return new Verified<>(appliedEntity);
        } else {
            return new Checking(verifications.size());
        }
    }

    @SneakyThrows
    private ENTITY convertToObj(Tracker tracker) {
        return MAPPER.readValue(tracker.getData(), new TypeReference<ENTITY>() {
            @Override
            @SneakyThrows
            public Type getType() {
                return Class.forName(tracker.getEntity());
            }
        });
    }

    @SneakyThrows
    private String convertToString(Object any) {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(any);
    }
}








