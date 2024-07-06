package uz.sardorbroo.tracker;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class TrackerPredications {

    private final Map<String, Predicate<String>> predications = new HashMap<>();

    public boolean add(String id, Predicate<String> predication) {

        // validate arguments, return false if something wrong

        predications.put(id, predication);
        return true;
    }

    public Predicate<String> getById(String id) {

        // validate arguments, return null if something wrong

        return predications.get(id);
    }

}
