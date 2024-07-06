package uz.sardorbroo.tracker.repository;

import uz.sardorbroo.tracker.domain.Verification;

import java.util.List;
import java.util.UUID;

public interface VerificationRepository extends CommonDML<Verification, UUID>, CommonDDL<Verification, UUID> {

    List<Verification> getAllVerificationsByTrackerId(UUID trackerId);

}
