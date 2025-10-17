package org.example.oop.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.fatboyindustrial.gsonjavatime.Converters;
import org.example.oop.Config.AppConfig;

import org.example.oop.Models.AlarmInterface;
import org.example.oop.Models.AlarmInterfaceAdapter;
import org.example.oop.Models.RegularAlarm;
import org.example.oop.Models.RepeatingAlarm;
import org.example.oop.Models.SnoozeAlarm;
import org.example.oop.Utils.Utils;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


public class AlarmManager implements AlarmManagerInterface {
    private List<AlarmInterface> alarmList = new ArrayList<>();
    private long nextId = 1;
    private final Gson gson;
    private static final String ALARMS_FILE = AppConfig.ALARMS_FILE;

    private final Map<Long, LocalDateTime> snoozeLastTriggered = new HashMap<>();

    public AlarmManager() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = Converters.registerAll(gsonBuilder);
        gsonBuilder.registerTypeAdapter(AlarmInterface.class, new AlarmInterfaceAdapter());
        this.gson = gsonBuilder.create();
        this.loadAlarms();
    }

    //Для RegularAlarm
    @Override
    public AlarmInterface addAlarm(LocalTime time, boolean active, String melody, String name) {
        AlarmInterface alarm = new RegularAlarm(nextId++, time, active, melody, name);
        alarmList.add(alarm);
        saveAlarms();
        return alarm;
    }

    //Для RepeatingAlarm
    @Override
    public AlarmInterface addAlarm(LocalTime time, boolean active, String melody, String name, Set<DayOfWeek> days) {
        AlarmInterface alarm = new RepeatingAlarm(nextId++, time, active, melody, name, days);
        alarmList.add(alarm);
        saveAlarms();
        return alarm;
    }

    //Для SnoozeAlarm
    @Override
    public AlarmInterface addAlarm(LocalTime time, boolean active, String melody, String name, Long parentID) {
        AlarmInterface alarm = new SnoozeAlarm(nextId++, time, active, melody, name, parentID);
        alarmList.add(alarm);
        saveAlarms();
        return alarm;
    }

    private Optional<SnoozeAlarm> findSnoozeAlarmFor(Long parentAlarmId) {
        for (AlarmInterface alarm: alarmList) {
            if (!(alarm instanceof SnoozeAlarm)) {
                continue;
            }
            if (parentAlarmId.equals(((SnoozeAlarm) alarm).getParentAlarmId())) {
                return Optional.of((SnoozeAlarm) alarm);
            }
        }
        return Optional.empty();
    }


    @Override
    public void deleteAlarm(Long id) {
        Optional<AlarmInterface> alarmExist = getAlarmById(id);
        if (alarmExist.isPresent()) {
            AlarmInterface alarm = alarmExist.get();

            switch (alarm) {
                case RegularAlarm _, RepeatingAlarm _ -> deleteSnoozeAlarmFor(alarm.getId());
                case SnoozeAlarm snooze -> handleSnoozeAlarmDeletion(snooze);
                default -> throw new IllegalStateException("Unexpected value: " + alarm);
            }
            alarmList.remove(alarm);
            snoozeLastTriggered.remove(id);
            saveAlarms();
        }
    }

    private void handleSnoozeAlarmDeletion(SnoozeAlarm alarm) {
        getAlarmById(alarm.getParentAlarmId()).ifPresent(parentAlarm -> {
            switch (parentAlarm) {
                case RegularAlarm _ -> parentAlarm.setActive(false);
                case RepeatingAlarm _ -> {
                }
                default -> throw new IllegalStateException("Unexpected value: " + parentAlarm);
            }
        });
    }

    private void deleteSnoozeAlarmFor(Long parentAlarmId) {
        findSnoozeAlarmFor(parentAlarmId).ifPresent(alarm -> {
            alarmList.remove(alarm);
            snoozeLastTriggered.remove(alarm.getId());
        });
    }

    @Override
    public void updateAlarm(Long id, LocalTime time, String melody, String name) {
        deleteSnoozeAlarmFor(id);

        getAlarmById(id).ifPresent(alarm -> {
            alarm.setTime(time);
            alarm.setMelody(melody);
            alarm.setName(name);
        });

        saveAlarms();
    }

    @Override
    public void updateAlarm(Long id, LocalTime time, String melody, String name, Set<DayOfWeek> days) {
        deleteSnoozeAlarmFor(id);

        getAlarmById(id).ifPresent(alarm -> {
            alarm.setTime(time);
            alarm.setMelody(melody);
            alarm.setName(name);
            if (days != null && alarm instanceof RepeatingAlarm repeatingAlarm) {
                repeatingAlarm.setRepeatDays(days);
            }
            if (days != null && days.isEmpty()) {
                alarm.setActive(false);
            }
        });

        saveAlarms();
    }

    @Override
    public void updateAlarmStatus(Long id, boolean active) {
        Optional<AlarmInterface> alarmExist = getAlarmById(id);
        if (alarmExist.isPresent()) {
            AlarmInterface alarm = alarmExist.get();
            alarm.setActive(active);
            if (!active) {
               handleAlarmDeactivation(alarm);
            }
            saveAlarms();
        }
    }

    private void handleAlarmDeactivation(AlarmInterface alarm) {
        switch (alarm) {
            case SnoozeAlarm snoozeAlarm -> handleSnoozeAlarmDeactivation(snoozeAlarm);
            case RegularAlarm _, RepeatingAlarm _-> deleteSnoozeAlarmFor(alarm.getId());
            default -> throw new IllegalStateException("Unexpected value: " + alarm);
        }
    }

    private void handleSnoozeAlarmDeactivation(SnoozeAlarm snoozeAlarm) {
        getAlarmById((snoozeAlarm).getParentAlarmId()).ifPresent(parentAlarm -> {
            switch (parentAlarm) {
                case RegularAlarm regularParent -> {
                    parentAlarm.setActive(false);
                    deleteSnoozeAlarmFor(parentAlarm.getId());
                }
                case RepeatingAlarm repeatingParent -> deleteSnoozeAlarmFor(parentAlarm.getId());
                default -> throw new IllegalStateException("Unexpected value: " + parentAlarm);
            }
        });
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
        LocalDateTime now = LocalDateTime.now();

        List<AlarmInterface> listAlarmsToRing = new ArrayList<>();
        for (AlarmInterface alarm: alarmList) {
            if (!alarm.shouldRingToday()) {
                continue;
            }

            LocalDateTime lastTime = snoozeLastTriggered.get(alarm.getId());
            if (lastTime != null && lastTime.plusMinutes(2).isAfter(now)) {
                continue;
            }
            listAlarmsToRing.add(alarm);
        }

        return listAlarmsToRing;
    }

    @Override
    public void markAlarmAsTriggered(AlarmInterface alarm) {
        snoozeLastTriggered.put(alarm.getId(), LocalDateTime.now());
    }

    @Override
    public void saveAlarms() {
        try {
            File file = new File(ALARMS_FILE);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                Type listType = new TypeToken<List<AlarmInterface>>(){}.getType();
                gson.toJson(alarmList, listType, writer);
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
            this.nextId = 1;
            return;
        }

        try (FileReader fileReader = new FileReader(file)) {
            Type listType = new TypeToken<List<AlarmInterface>>(){}.getType();
            List<AlarmInterface> alarms = gson.fromJson(fileReader, listType);
            this.alarmList = alarms != null ? alarms : new ArrayList<>();

            this.nextId = alarmList.stream().mapToLong(AlarmInterface::getId).max().orElse(0) + 1;

        } catch (IOException e) {
            Utils.showError("Ошибка загрузки JSON: " + e.getMessage());
            this.alarmList = new ArrayList<>();
            this.nextId = 1;
        } catch (JsonSyntaxException e) {
            Utils.showError("Невалидный JSON: " + e.getMessage());
            this.alarmList = new ArrayList<>();
            this.nextId = 1;
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
