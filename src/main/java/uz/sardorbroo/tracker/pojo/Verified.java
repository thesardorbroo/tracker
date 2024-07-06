package uz.sardorbroo.tracker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Verified<T> implements VerificationResult {

    private T data;

}
