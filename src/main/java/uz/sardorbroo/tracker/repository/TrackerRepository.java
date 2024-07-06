package uz.sardorbroo.tracker.repository;

import uz.sardorbroo.tracker.domain.Tracker;

import java.util.UUID;

public interface TrackerRepository extends CommonDDL<Tracker, UUID>, CommonDML<Tracker, UUID> {
}
