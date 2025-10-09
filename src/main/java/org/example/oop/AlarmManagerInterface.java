package org.example.oop;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AlarmManagerInterface {
    AlarmInterface addAlarm(LocalTime time, boolean active, String melody, String name);
    AlarmInterface addAlarm(LocalTime time, boolean active, String melody);
    void deleteAlarm(Long id);
    void updateAlarmStatus(Long id, boolean active);

    List<AlarmInterface> getAllAlarms();
    List<AlarmInterface> getActiveAlarms();
    Optional<AlarmInterface> getAlarmById(Long id);
    List<AlarmInterface> getAlarmsToRing();

    void saveAlarms();
    void loadAlarms();

    int getAlarmCount();
    boolean hasAlarm();

}
