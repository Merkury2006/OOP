package org.example.oop.Models;

import java.time.LocalTime;

public interface AlarmInterface {
    Long getId();

    void setTime(LocalTime time);

    boolean isActive();
    void setActive(boolean active);

    default String getDisplayName() {
        return getName();
    }

    default boolean isEditable() {
        return true;
    }

    String getName();

    LocalTime getTime();

    void setName(String name);

    String getMelody();

    void setMelody(String melody);

    boolean shouldRingToday();

    AlarmType getType();
}
