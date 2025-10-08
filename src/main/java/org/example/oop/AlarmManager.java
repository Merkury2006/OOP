package org.example.oop;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlarmManager implements AlarmManagerInterface {
    private List<AlarmInterface> alarmList = new ArrayList<>();
    private long nextId = 1;

    public AlarmManager() {
        loadAlarms();
    }

    @Override
    public AlarmInterface addAlarm(LocalTime time, boolean active, String s) {
        AlarmInterface alarm = new Alarm(nextId++, time, active, s);
        alarmList.add(alarm);
        saveAlarms();
        return alarm;
    }

    @Override
    public AlarmInterface addAlarm(LocalTime time, boolean active) {
        AlarmInterface alarm = new Alarm(nextId++, time,active);
        alarmList.add(alarm);
        saveAlarms();
        return alarm;
    }

    @Override
    public void deleteAlarm(Long id) {
        boolean removed = alarmList.removeIf(alarm -> alarm.getId().equals(id));
        if (removed) {
            saveAlarms();
        }
    }

    @Override
    public void updateAlarmStatus(Long id, boolean active) {
        for (AlarmInterface alarm: alarmList) {
            if (alarm.getId().equals(id)) {
                alarm.setActive(active);
                saveAlarms();
                return;
            }
        }
    }

    @Override
    public List<AlarmInterface> getAllAlarms() {
        return List.copyOf(alarmList);
    }

    @Override
    public List<AlarmInterface> getActiveAlarms() {
        return alarmList.stream().filter(AlarmInterface::isActive).toList();
    }

    @Override
    public Optional<AlarmInterface> getAlarmById(Long id) {
        return alarmList.stream().filter(alarm -> alarm.getId().equals(id)).findFirst();
    }

    @Override
    public List<AlarmInterface> getAlarmsToRing() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        List<AlarmInterface> listAlarmsToRing = new ArrayList<>();
        for (AlarmInterface alarm: alarmList) {
            if (alarm.isActive() && alarm.getTime().equals(now)) {
                listAlarmsToRing.add(alarm);
            }
        }
        return listAlarmsToRing;
    }

    @Override
    public void saveAlarms() {

    }

    @Override
    public void loadAlarms() {

    }

    @Override
    public int getAlarmCount() {
        return alarmList.size();
    }

    @Override
    public boolean hasAlarm() {
        return !alarmList.isEmpty();
    }
}
