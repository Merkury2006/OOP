package org.example.oop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.fatboyindustrial.gsonjavatime.Converters;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlarmManager implements AlarmManagerInterface {
    private List<AlarmInterface> alarmList = new ArrayList<>();
    private long nextId = 1;
    private final Gson gson;
    private static final String ALARMS_FILE = "data/alarms.json";

    public AlarmManager() {
        this.gson = Converters.registerLocalTime(new GsonBuilder()).create();
        this.loadAlarms();
    }

    @Override
    public AlarmInterface addAlarm(LocalTime time, boolean active, String melody, String name) {
        AlarmInterface alarm = new Alarm(nextId++, time, active, melody, name);
        alarmList.add(alarm);
        saveAlarms();
        return alarm;
    }

    @Override
    public AlarmInterface addAlarm(LocalTime time, boolean active, String melody) {
        AlarmInterface alarm = new Alarm(nextId++, time,active, melody);
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
        try {
            File file = new File(ALARMS_FILE);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(alarmList, writer);
            }
        } catch (IOException e) {
            Utils.showError("Ошибка сохранения JSON: " + e.getMessage());
        }
    }

    @Override
    public void loadAlarms() {
        File file = new File(ALARMS_FILE);

        if (!file.exists() || file.length() == 0) {
            this.alarmList = new ArrayList<>();
            return;
        }

        try (FileReader fileReader = new FileReader(file)) {
            Type listType = new TypeToken<List<Alarm>>(){}.getType();
            List<AlarmInterface> alarms = gson.fromJson(fileReader, listType);

            if (alarms == null) {
                this.alarmList = new ArrayList<>();
                return;
            }
            this.alarmList = alarms;

        } catch (IOException e) {
            Utils.showError("Ошибка загрузки JSON: " + e.getMessage());
            this.alarmList =  new ArrayList<>();
        } catch (com.google.gson.JsonSyntaxException e) {
            Utils.showError("Невалидный JSON: " + e.getMessage());
            this.alarmList = new ArrayList<>();
        }
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
