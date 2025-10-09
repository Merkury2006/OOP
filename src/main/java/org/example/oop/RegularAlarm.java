package org.example.oop;

import java.time.LocalDate;
import java.time.LocalTime;

public class RegularAlarm extends Alarm {
    private LocalDate alarmDate;

    public RegularAlarm(Long ID, LocalTime time, boolean isActive, String melody, String name) {
        super(ID, time, isActive, melody, name);
        this.alarmDate = LocalDate.now();
    }

    public RegularAlarm(Long ID, LocalTime time, boolean isActive, String melody) {
        this(ID, time, isActive, melody, "Будильник");
    }

    @Override
    public AlarmType getType() {
        return AlarmType.REGULAR;
    }

    @Override
    public boolean shouldRingToday() {
        return super.isActive() && alarmDate.equals(LocalDate.now()) && super.isTimeToRingNow();
    }

    public LocalDate getAlarmDate() {return alarmDate;}

    public void setAlarmDate(LocalDate alarmDate) {
        this.alarmDate = alarmDate;
    }
}
