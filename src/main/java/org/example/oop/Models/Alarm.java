package org.example.oop.Models;

import java.time.LocalTime;

public abstract class Alarm implements AlarmInterface {
    public enum AlarmType {
        REGULAR,
        REPEATING,
        REMINDER,
        SNOOZE
    }

    protected Long id;
    protected LocalTime time;
    protected boolean active;
    protected String melody;
    protected String name;

    public Alarm(Long id, LocalTime time, boolean active, String melody, String name) {
        this.id = id;
        this.time = time;
        this.active = active;
        this.melody = melody;
        this.name = name;
    }

    @Override public Long getId() { return id; }

    @Override public LocalTime getTime() { return time; }
    @Override public void setTime(LocalTime time) { this.time = time; }

    @Override public boolean isActive() { return active; }
    @Override public void setActive(boolean active) { this.active = active; }

    @Override public String getName() {return name; }

    @Override public String getMelody() {return melody;}



    @Override public abstract boolean shouldRingToday();

    protected boolean isTimeToRingNow() {
        return time.getHour() == LocalTime.now().getHour() && time.getMinute() == LocalTime.now().getMinute();
    }

    @Override public abstract AlarmType getType();
}
