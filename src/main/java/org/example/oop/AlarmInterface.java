package org.example.oop;

import java.time.LocalTime;

public interface AlarmInterface {
    Long getId();

    boolean isActive();
    void setActive(boolean active);

    String getName();
    void setName(String name);

    LocalTime getTime();

}
