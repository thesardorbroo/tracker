package uz.sardorbroo.tracker.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrackerConfig {

    private UUID id;

    /**
     * For example:
     * Person
     */
    private String entity;

    /**
     * For example:
     * ROLE_BASED
     */
    private String predicationId;

    /**
     * For example:
     * 2 (It means 2 users should verify entity which has these roles)
     */
    private Integer verificationCount;

    /**
     * For example:
     * John Doe
     */
    private String createdBy;

    /**
     * For example:
     * Now
     */
    private Instant createdAt;
}
