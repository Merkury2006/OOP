package org.example.oop.Config;

public class AppConfig {
    public static final int SNOOZE_MINUTES = 5;
    public static final int ALARM_CHECK_INTERVAL_SECONDS = 1;
    public static final int SHUTDOWN_TIMEOUT_SECONDS = 5;


    public static final String ALARMS_FILE = "data/alarms.json";


    public static final String CHECK_ALARMS_FLOW_TITLE = "AlarmChecker";
    public static final String APP_TITLE = "Будульник";
    public static final String DEFAULT_ALARM_TITLE = "Будильник";
    public static final String ALARM_ADD_DIALOG_SERVICE_TITLE = "Новый будильник";
    public static final String ALARM_SNOOZE_PREFIX = ", отложенный от: ";

    private AppConfig() {}
}
