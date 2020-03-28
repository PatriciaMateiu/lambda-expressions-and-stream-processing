package src.main;

import java.util.List;
import java.util.Map;

public interface ActivityCounter {

    abstract Map<String, Long> count(List<String> list);
}
