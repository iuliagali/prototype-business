package ro.project.prototype.adapter.model;

import java.time.DayOfWeek;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHours {

  public List<Day> monday;
  public List<Day> tuesday;
  public List<Day> wednesday;
  public List<Day> thursday;
  public List<Day> friday;
  public List<Day> saturday;
  public List<Day> sunday;

  public List<Day> getScheduleOfDay(DayOfWeek dayOfWeek) {
    return switch (dayOfWeek) {
      case MONDAY -> getMonday();
      case TUESDAY -> getTuesday();
      case WEDNESDAY -> getWednesday();
      case THURSDAY -> getThursday();
      case FRIDAY -> getFriday();
      case SATURDAY -> getSaturday();
      case SUNDAY -> getSunday();
    };
  }
}

