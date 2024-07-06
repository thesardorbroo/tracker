package uz.sardorbroo.tracker.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import uz.sardorbroo.tracker.domain.enumeration.TrackingType;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Tracker {

    private UUID id;

    private String entity;

    private TrackingType type;

    private String data;

    private boolean verified = false;

    private String createBy;

    private Instant createAt;
}
