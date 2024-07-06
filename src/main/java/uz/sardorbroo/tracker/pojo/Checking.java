package uz.sardorbroo.tracker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Checking implements VerificationResult {

    private Integer verificationsCount;

}
