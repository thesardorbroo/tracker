package uz.sardorbroo.tracker.repository;

import uz.sardorbroo.tracker.domain.TrackerConfig;

import java.util.Optional;
import java.util.UUID;

public interface TrackerConfigRepository extends CommonDDL<TrackerConfig, UUID>, CommonDML<TrackerConfig, UUID> {

    Optional<TrackerConfig> getByEntity(String entity);

}
