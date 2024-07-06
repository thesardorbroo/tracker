package uz.sardorbroo.tracker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.TransactionalException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import uz.sardorbroo.tracker.TrackerConfigContext;
import uz.sardorbroo.tracker.TrackerPredications;
import uz.sardorbroo.tracker.domain.Storage;
import uz.sardorbroo.tracker.domain.Tracker;
import uz.sardorbroo.tracker.domain.TrackerConfig;
import uz.sardorbroo.tracker.domain.Verification;
import uz.sardorbroo.tracker.domain.enumeration.TrackingType;
import uz.sardorbroo.tracker.exception.AccessDeniedException;
import uz.sardorbroo.tracker.exception.NotFoundException;
import uz.sardorbroo.tracker.exception.TrackerException;
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

@RequiredArgsConstructor
public class TrackerProcessor<ENTITY, ID> {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    // private final TrackerConfigContext context = TrackerConfigContext.getInstance();
    private final Storage<ENTITY, ID> storage;
    private final TrackerRepository trackerRepository;
    private final TrackerConfigRepository trackerConfigRepository;
    private final VerificationRepository verificationRepository;
    private final TrackerPredications trackerPredications;

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

        trackerRepository.create(tracker);
        return tracker;
    }

    @SneakyThrows
    // make dynamic getting verifier ID
    public VerificationResult verify(UUID trackerId, String verifierId) throws RuntimeException {

        Tracker tracker = trackerRepository.getById(trackerId)
                .orElseThrow(() -> new NotFoundException("Tracker is not found by given ID! ID: " + trackerId));
        if (tracker.isVerified()) {
            throw new TrackerException("Tracker has already verified!");
        }

        TrackerConfig config = trackerConfigRepository.getByEntity(tracker.getEntity())
                .orElseThrow(() -> new NotFoundException("Tracker config is not found by given entity! Entity: " + tracker.getEntity()));

        Predicate<String> predication = trackerPredications.getById(config.getPredicationId());
        if (!predication.test(verifierId)) {
            throw new AccessDeniedException("You cannot verify this data!");
        }

        Verification verification = new Verification()
                .setId(UUID.randomUUID())
                .setTrackerId(trackerId)
                .setCreatedBy(verifierId)
                .setCreatedAt(Instant.now());

        verificationRepository.create(verification);

        List<Verification> verifications = verificationRepository.getAllVerificationsByTrackerId(tracker.getId());
        // make it dynamic
        if (verifications.size() == config.getVerificationCount()) {

            tracker.setVerified(true);
            trackerRepository.update(tracker);

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








