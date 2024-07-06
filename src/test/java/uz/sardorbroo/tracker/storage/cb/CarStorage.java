package uz.sardorbroo.tracker.storage.cb;

import org.springframework.stereotype.Service;
import uz.sardorbroo.tracker.domain.Car;
import uz.sardorbroo.tracker.domain.Storage;
import uz.sardorbroo.tracker.domain.enumeration.TrackingType;
import uz.sardorbroo.tracker.repository.CarRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class CarStorage implements Storage<Car, UUID> {
    private final CarRepository repository;
    private Map<TrackingType, Function<Car, Car>> executors;

    public CarStorage(CarRepository repository) {
        this.repository = repository;
        this.executors = Map.of(
                TrackingType.CREATE, this::create,
                TrackingType.UPDATE, this::modify,
                TrackingType.DELETE, this::remove
        );
    }

    @Override
    public Car create(Car car) {
        return repository.save(car);
    }

    @Override
    public Car modify(Car car) {
        return repository.save(car);
    }

    @Override
    public Car remove(Car car) {
        Car old = repository.findById(car.getId()).orElse(null);
        repository.delete(car);
        return old;
    }

    @Override
    public Car byId(UUID uuid) {
        return repository.findById(uuid).orElse(null);
    }

    @Override
    public Function<Car, Car> findMethodByTrackingType(TrackingType type) {
        return executors.get(type);
    }

    @Override
    public List<Car> all() {
        return repository.findAll();
    }
}
