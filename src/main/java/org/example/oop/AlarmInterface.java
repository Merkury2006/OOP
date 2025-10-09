package org.example.oop;

import java.time.LocalTime;

public interface AlarmInterface {
    Long getId();

    void setTime(LocalTime time);

    boolean isActive();
    void setActive(boolean active);

    String getName();

    LocalTime getTime();

    String getMelody();

    boolean shouldRingToday();

    Alarm.AlarmType getType();
}
