package uz.sardorbroo.tracker.domain;

import uz.sardorbroo.tracker.domain.enumeration.TrackingType;

import java.util.List;
import java.util.function.Function;

public interface Storage<ENTITY, ID> {

    ENTITY create(ENTITY entity);

    ENTITY modify(ENTITY entity);

    ENTITY remove(ENTITY entity);

    ENTITY byId(ID id);

    Function<ENTITY, ENTITY> findMethodByTrackingType(TrackingType type);

    List<ENTITY> all();
}
