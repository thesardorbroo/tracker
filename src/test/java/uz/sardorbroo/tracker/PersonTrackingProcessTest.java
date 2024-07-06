package uz.sardorbroo.tracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uz.sardorbroo.tracker.domain.Person;
import uz.sardorbroo.tracker.domain.Storage;
import uz.sardorbroo.tracker.domain.Tracker;
import uz.sardorbroo.tracker.domain.TrackerConfig;
import uz.sardorbroo.tracker.pojo.Verified;
import uz.sardorbroo.tracker.repository.TrackerProcessor;
import uz.sardorbroo.tracker.storage.cb.PersonStorage;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

class PersonTrackingProcessTest {
    private static final String USER_ID = "ADMIN";
    private static final String PREDICATION_ID = String.valueOf(UUID.randomUUID());
    private final TrackerConfigContext context = TrackerConfigContext.getInstance();
    private final Storage<Person, UUID> storage;
    private final TrackerProcessor<Person, UUID> processor;

    public PersonTrackingProcessTest() {
        this.storage = new PersonStorage();
        Predicate<String> predication = userId -> Objects.equals(USER_ID, userId);
        TrackerPredications trackerPredications = new TrackerPredications();
        trackerPredications.add(PREDICATION_ID, predication);
        this.processor = new TrackerProcessor<>(storage, trackerPredications);
    }

    @Test
    void testUpdatePersonWithVerifyWithValidParams() throws ClassNotFoundException {
        TrackerConfig personTrackerConfig = new TrackerConfig()
                .setId(UUID.randomUUID())
                .setCreatedAt(Instant.now())
                .setCreatedBy(USER_ID)
                .setEntity(Person.class.getName())
                .setPredicationId(PREDICATION_ID)
                .setVerificationCount(1);

        context.add(personTrackerConfig);

        Person alex = new Person(null, "Alex", 18);
        alex = storage.create(alex);

        Person alexDataCopy = alex;
        alexDataCopy.setAge(25);
        Tracker tracker = processor.update(alexDataCopy, () -> "ID of creator");

        Verified<Person> result = (Verified<Person>) processor.verify(tracker.getId(), USER_ID);
        var alexFromStorage = storage.byId(result.getData().getId());

        Assertions.assertNotNull(alexFromStorage);
        System.out.println("Old data: " + alex);
        System.out.println("Person " + alexFromStorage);
        System.out.println("Storage" + storage.all());

    }

    @Test
    void testCreatePersonWithVerifyUnauthorizedUser() throws ClassNotFoundException {
        TrackerConfig personTrackerConfig = new TrackerConfig()
                .setId(UUID.randomUUID())
                .setCreatedAt(Instant.now())
                .setCreatedBy(USER_ID)
                .setEntity(Person.class.getName())
                .setPredicationId(PREDICATION_ID)
                .setVerificationCount(1);

        context.add(personTrackerConfig);

        Person alex = new Person(null, "Alex", 18);

        Tracker tracker = processor.create(alex, () -> "ID of creator");

        Assertions.assertThrows(RuntimeException.class, () -> storage.byId(alex.getId()));
        Assertions.assertNotNull(context.get(tracker.getId()));

        Assertions.assertThrows(RuntimeException.class, () -> processor.verify(tracker.getId(), "Another user"));
        System.out.println("Storage: " + storage.all());
    }

    @Test
    void testCreatePersonWithValidParams() throws ClassNotFoundException {

        TrackerConfig personTrackerConfig = new TrackerConfig()
                .setId(UUID.randomUUID())
                .setCreatedAt(Instant.now())
                .setCreatedBy(USER_ID)
                .setEntity(Person.class.getName())
                .setPredicationId(PREDICATION_ID)
                .setVerificationCount(1);

        context.add(personTrackerConfig);

        Person alex = new Person(null, "Alex", 18);

        Tracker tracker = processor.create(alex, () -> "ID of creator");

        Assertions.assertThrows(RuntimeException.class, () -> storage.byId(alex.getId()));
        Assertions.assertNotNull(context.get(tracker.getId()));

        Verified<Person> result = (Verified<Person>) processor.verify(tracker.getId(), USER_ID);
        var alexFromStorage = storage.byId(result.getData().getId());

        Assertions.assertNotNull(alexFromStorage);
        System.out.println("Person " + alexFromStorage);
        System.out.println("Storage" + storage.all());
        System.out.println("Configs: " + context.getAllConfigs());
        System.out.println("Trackers: " + context.getAllTrackers());
        System.out.println("Verifications: " + context.getAllVerifications());
    }
}
