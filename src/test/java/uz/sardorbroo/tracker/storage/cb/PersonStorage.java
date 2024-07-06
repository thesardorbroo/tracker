package uz.sardorbroo.tracker.storage.cb;

import uz.sardorbroo.tracker.domain.Person;
import uz.sardorbroo.tracker.domain.Storage;
import uz.sardorbroo.tracker.domain.enumeration.TrackingType;

import java.util.*;
import java.util.function.Function;

public class PersonStorage implements Storage<Person, UUID> {
    private final Map<TrackingType, Function<Person, Person>> executors;
    private final Map<UUID, Person> storage = new HashMap<>();

    public PersonStorage() {
        this.executors = Map.of(
                TrackingType.CREATE, this::create,
                TrackingType.UPDATE, this::modify,
                TrackingType.DELETE, this::remove
        );
    }

    @Override
    public Person create(Person person) {

        if (Objects.isNull(person)) {
            throw new RuntimeException("Invalid argument is passed! Person must not be null!");
        }

        UUID id = UUID.randomUUID();
        person.setId(id);
        storage.put(id, person);

        return person;
    }

    @Override
    public Person modify(Person person) {

        if (Objects.isNull(person)) {
            throw new RuntimeException("Invalid argument is passed! Person must not be null!");
        }

        if (Objects.isNull(person.getId())) {
            throw new RuntimeException("Invalid argument is passed! Person ID must not be null!");
        }

        if (!storage.containsKey(person.getId())) {
            throw new RuntimeException("Person is not exist by given ID");
        }

        storage.put(person.getId(), person);
        return person;
    }

    @Override
    public Person remove(Person p) {

        Person person = byId(p.getId());
        storage.remove(p.getId());
        return person;
    }

    @Override
    public Person byId(UUID id) {
        if (Objects.isNull(id)) {
            throw new RuntimeException("Invalid argument is passed! Person ID must not be null!");
        }

        if (!storage.containsKey(id)) {
            throw new RuntimeException("Person is not exist by given ID");
        }

        return storage.get(id);
    }

    @Override
    public Function<Person, Person> findMethodByTrackingType(TrackingType type) {
        return executors.get(type);
    }

    @Override
    public List<Person> all() {
        return storage.values().stream().toList();
    }
}
