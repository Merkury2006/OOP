package org.example.oop.Managers;

import org.example.oop.Models.AlarmInterface;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AlarmManagerInterface {
    AlarmInterface addAlarm(LocalTime time, boolean active, String melody, String name);
    AlarmInterface addAlarm(LocalTime time, boolean active, String melody, String name, Set<DayOfWeek> days);
    AlarmInterface addAlarm(LocalTime time, boolean active, String melody, String name, Long parentID);

    void deleteAlarm(Long id);


    void updateAlarmStatus(Long id, boolean active);


    List<AlarmInterface> getAllAlarms();
    List<AlarmInterface> getActiveAlarms();
    Optional<AlarmInterface> getAlarmById(Long id);
    List<AlarmInterface> getAlarmsToRing();

    void markAlarmAsTriggered(AlarmInterface alarm);

    void saveAlarms();
    void loadAlarms();

    int getAlarmCount();
    boolean hasAlarm();

}
