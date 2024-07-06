package uz.sardorbroo.tracker.repository;

import java.util.Collection;

public interface CommonDML<ENTITY, ID> {

    ENTITY create(ENTITY entity);


    ENTITY update(ENTITY entity);

    ENTITY delete(ENTITY entity);

    ENTITY delete(Collection<ENTITY> entities);

}
