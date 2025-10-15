package org.example.oop.Models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.StringJoiner;

public class RepeatingAlarm extends Alarm{
    private final Set<DayOfWeek> repeatDays;

    public RepeatingAlarm(Long id, LocalTime time, boolean active, String melody, String name, Set<DayOfWeek> repeatDays) {
        super(id, time, active, melody, name);
        this.repeatDays = repeatDays;
    }

    @Override
    public String getName() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        repeatDays.stream().sorted().forEach(day -> {
            switch (day) {
                case MONDAY -> stringJoiner.add("Пон");
                case TUESDAY -> stringJoiner.add("Вт");
                case WEDNESDAY -> stringJoiner.add("Ср");
                case THURSDAY -> stringJoiner.add("Чт");
                case FRIDAY -> stringJoiner.add("Пт");
                case SATURDAY -> stringJoiner.add("Сб");
                case SUNDAY -> stringJoiner.add("Вс");
            }
        });

        return name + "(" + stringJoiner + ")";
    }

    @Override
    public boolean shouldRingToday() {
        return super.isActive() && repeatDays.contains(LocalDate.now().getDayOfWeek()) && super.isTimeToRingNow();
    }

    @Override
    public AlarmType getType() {
        return AlarmType.REPEATING;
    }
}
