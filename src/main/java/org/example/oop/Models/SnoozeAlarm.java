package org.example.oop.Models;

import java.time.LocalTime;

public class SnoozeAlarm extends Alarm {
    private final Long parentAlarmId;

    public SnoozeAlarm(Long id, LocalTime time, boolean active, String melody, String name, Long parentAlarmId) {
        super(id, time, active, melody, name);
        this.parentAlarmId = parentAlarmId;
    }

    @Override
    public AlarmType getType() {
        return AlarmType.SNOOZE;
    }

    @Override
    public boolean shouldRingToday() {
        return super.isActive() && super.isTimeToRingNow();
    }

    public void reschedule(LocalTime newTime) {
        super.setTime(newTime);
    }

    public Long getParentAlarmId() {return parentAlarmId;}
}
