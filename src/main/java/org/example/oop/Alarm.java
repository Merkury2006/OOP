package org.example.oop;

import java.time.LocalTime;

public class Alarm implements AlarmInterface {
    private final Long ID;
    private final LocalTime time;
    private boolean isActive;
    private String name;


    public Alarm(Long ID, LocalTime time, boolean isActive, String name) {
        this.ID = ID;
        this.time = time;
        this.isActive = isActive;
        this.name = name;
    }

    public Alarm(Long ID, LocalTime time, boolean isActive) {
        this.ID = ID;
        this.time = time;
        this.isActive = isActive;
        this.name = "Будильник";
    }

    @Override
    public Long getId() {
        return this.ID;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LocalTime getTime() {
        return this.time;
    }
}
