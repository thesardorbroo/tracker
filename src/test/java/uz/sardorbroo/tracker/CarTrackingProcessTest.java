package uz.sardorbroo.tracker;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import uz.sardorbroo.tracker.domain.*;
import uz.sardorbroo.tracker.pojo.Verified;
import uz.sardorbroo.tracker.repository.TrackerProcessor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Testcontainers
@SpringBootTest
public class CarTrackingProcessTest {
    private static final List<String> ALLOWED_USER_IDS = List.of("admin", "user", "hr");
    private static final String USER_ID = "hr";
    private static final String PREDICATION_ID = "";
    private final TrackerConfigContext context;

    private final TrackerPredications trackerPredications;

    @Autowired
    private Storage<Car, UUID> storage;

    @Container
    @ServiceConnection(type = JdbcConnectionDetails.class)
    static PostgreSQLContainer<?> psql = new PostgreSQLContainer<>("postgres:14.10");

    public CarTrackingProcessTest() {
        this.context = TrackerConfigContext.getInstance();
        Predicate<String> predication = ALLOWED_USER_IDS::contains;
        TrackerPredications trackerPredications = new TrackerPredications();
        trackerPredications.add(PREDICATION_ID, predication);
        this.trackerPredications = trackerPredications;
    }

    @Test
    @Transactional
    public void test() throws ClassNotFoundException {
        TrackerProcessor processor = new TrackerProcessor<>(storage, trackerPredications);

        TrackerConfig personTrackerConfig = new TrackerConfig()
                .setId(UUID.randomUUID())
                .setCreatedAt(Instant.now())
                .setCreatedBy(USER_ID)
                .setEntity(Car.class.getName())
                .setPredicationId(PREDICATION_ID)
                .setVerificationCount(1);

        context.add(personTrackerConfig);

        Car bmw = new Car(UUID.randomUUID(), "BMW", "black", "Sedan");

        Tracker tracker = processor.create(bmw, () -> "Some body");

        Assertions.assertNull(storage.byId(bmw.getId()));
        Assertions.assertNotNull(context.get(tracker.getId()));

        Verified<Car> result = (Verified<Car>) processor.verify(tracker.getId(), USER_ID);
        var bmwFromStorage = storage.byId(result.getData().getId());

        Assertions.assertNotNull(bmwFromStorage);
        System.out.println("Car " + bmwFromStorage);
        System.out.println("Storage" + storage.all());
        System.out.println("Configs: " + context.getAllConfigs());
        System.out.println("Trackers: " + context.getAllTrackers());
        System.out.println("Verifications: " + context.getAllVerifications());

    }


}
