package ro.project.prototype.domain.port;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import com.fasterxml.jackson.datatype.jsr310.ser.MonthDaySerializer;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.project.prototype.adapter.model.BusinessEntity;
import ro.project.prototype.adapter.model.BusinessSchedule;
import ro.project.prototype.adapter.model.Day;
import ro.project.prototype.adapter.model.OpeningHours;
import ro.project.prototype.application.DateUtils;
import ro.project.prototype.domain.model.Interval;
import ro.project.prototype.domain.model.OpeningHoursByDay;
import ro.project.prototype.domain.model.Place;
import ro.project.prototype.domain.port.client.BusinessEntitiesApi;
import ro.project.prototype.domain.service.PlaceService;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {

  @InjectMocks
  private PlaceService underTest;

  @Mock
  BusinessEntitiesApi businessEntitiesApi;

  @Test
  public void testGetPlaceById_idNotNull() {
    BusinessEntity businessEntity = buildBusinessEntity(buildOpeningHoursClosedOnWeekend());
    Mockito.when(businessEntitiesApi.getBusinessById(eq("id"))).thenReturn(businessEntity);

    Place result = underTest.getPlaceById("id");

    assertEquals(result.getName(), businessEntity.getDisplayedWhat());
    assertEquals(result.getAddress(), businessEntity.getDisplayedWhere());
    Arrays.stream(DayOfWeek.values())
        .forEach(dayOfWeek -> assertTrue(compareOpeningHoursByDay(dayOfWeek, result.getOpeningHours(),
            businessEntity.getBusinessSchedule().getHours())));
  }

  private boolean compareOpeningHoursByDay(DayOfWeek dayOfWeek, List<OpeningHoursByDay> placeOpeningHours,
      OpeningHours businessOpeningHours) {
    OpeningHoursByDay placeOpeningHoursByDay = placeOpeningHours.stream()
        .filter(openingHoursByDay -> openingHoursByDay.getDayOfWeek().equals(dayOfWeek)).findFirst().orElse(null);
    List<Day> businessOpeningHoursByDay = businessOpeningHours.getScheduleOfDay(dayOfWeek);
    if (isClosedDay(placeOpeningHoursByDay, businessOpeningHoursByDay)) {
      return true;
    }
    return compareHours(placeOpeningHoursByDay.getInterval(), businessOpeningHoursByDay);
  }

  private boolean isClosedDay(OpeningHoursByDay placeOpeningHoursByDay, List<Day> businessOpeningHoursByDay) {
    return Objects.isNull(businessOpeningHoursByDay) && placeOpeningHoursByDay.getInterval().size() == 0;
  }

  private boolean compareHours(List<Interval> placeOpeningHoursByDay, List<Day> businessOpeningHoursByDay) {
    if (Objects.isNull(placeOpeningHoursByDay) && Objects.isNull(businessOpeningHoursByDay)) {
      return true;
    }

    if (placeOpeningHoursByDay.size() != businessOpeningHoursByDay.size()) {
      return false;
    }

    List<Interval> placeOpeningHoursByDaySorted = placeOpeningHoursByDay.stream()
        .sorted(Comparator.comparing(Interval::getFrom)).toList();
    List<Day> businessOpeningHoursByDaySorted = businessOpeningHoursByDay.stream()
        .sorted(Comparator.comparing(Day::getStart)).toList();

    return IntStream.range(0, placeOpeningHoursByDay.size())
        .allMatch(value -> placeOpeningHoursByDaySorted.get(value).getFrom().toString()
            .equals(businessOpeningHoursByDaySorted.get(value).getStart()));
  }

  private BusinessEntity buildBusinessEntity(OpeningHours openingHours) {
    BusinessSchedule businessSchedule = BusinessSchedule.builder().hours(openingHours).build();
    return BusinessEntity.builder().displayedWhat("name").displayedWhere("address").businessSchedule(
        businessSchedule).build();
  }

  private OpeningHours buildOpeningHoursClosedOnWeekend() {
    return OpeningHours.builder().monday(List.of(buildEarlyShortDay(), buildLateShortDay()))
        .tuesday(Collections.emptyList())
        .wednesday(Collections.emptyList()).thursday(List.of(buildNormalDay())).friday(Collections.emptyList())
        .build();
  }

  private Day buildNormalDay() {
    return new Day("08:00", "17:00");
  }

  private Day buildEarlyShortDay() {
    return new Day("08:00", "10:00");
  }

  private Day buildLateShortDay() {
    return new Day("15:00", "17:00");
  }

  private Place buildPlace(List<OpeningHoursByDay> openingHoursByDay) {
    return Place.builder().name("name").address("address").openingHours(openingHoursByDay).build();
  }

  private Interval buildNormalInterval() {
    return new Interval(DateUtils.getHour("08:00"), DateUtils.getHour("17:00"));
  }

  private Interval buildEarlyShortInterval() {
    return new Interval(DateUtils.getHour("08:00"), DateUtils.getHour("10:00"));
  }

  private Interval buildLateShortInterval() {
    return new Interval(DateUtils.getHour("15:00"), DateUtils.getHour("17:00"));
  }
}
