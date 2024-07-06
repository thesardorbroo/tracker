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
public class Verification {

    private UUID id;

    private UUID trackerId;

    private String createdBy;

    private Instant createdAt;
}
