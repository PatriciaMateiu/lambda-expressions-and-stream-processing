package src.main;

import pack.MonitoredData;

import java.util.List;
import java.util.Map;

public interface Counter {

    abstract int count(List<MonitoredData> list);

}
