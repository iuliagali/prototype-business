package ro.project.prototype.application;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
  private final static String TIME_PATTERN = "HH:mm";

  public static LocalTime getHour(String hour) {
    DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern(TIME_PATTERN);
    return LocalTime.parse(hour, simpleDateFormat);
  }
}
