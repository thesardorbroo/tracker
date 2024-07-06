package uz.sardorbroo.tracker.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommonDDL<ENTITY, ID> {

    Optional<ENTITY> getById(ID id);

    List<ENTITY> getAll();

    List<ENTITY> getAll(Collection<ID> ids);
}
