package ro.project.prototype.domain.service;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.project.prototype.adapter.model.BusinessEntity;
import ro.project.prototype.adapter.model.BusinessSchedule;
import ro.project.prototype.adapter.model.Day;
import ro.project.prototype.adapter.model.OpeningHours;
import ro.project.prototype.application.DateUtils;
import ro.project.prototype.domain.model.Interval;
import ro.project.prototype.domain.model.OpeningHoursByDay;
import ro.project.prototype.domain.model.Place;
import ro.project.prototype.domain.port.client.BusinessEntitiesApi;

@Component
public class PlaceService {

  private final BusinessEntitiesApi businessEntitiesApi;

  @Autowired
  public PlaceService(BusinessEntitiesApi businessEntitiesApi) {
    this.businessEntitiesApi = businessEntitiesApi;
  }

  public Place getPlaceById(String id) {
    BusinessEntity businessEntityById = businessEntitiesApi.getBusinessById(id);

    return mapBusinessToPlace(businessEntityById);
  }

  private Place mapBusinessToPlace(BusinessEntity businessEntity) {
    BusinessSchedule businessSchedule = businessEntity.getBusinessSchedule();
    OpeningHours openingHours = businessSchedule.getHours();
    List<OpeningHoursByDay> openingHoursByDays = buildOpeningHoursPerDays(openingHours);

    return createPlace(businessEntity, openingHoursByDays);
  }

  private List<OpeningHoursByDay> buildOpeningHoursPerDays(OpeningHours businessOpeningHoursSchedule) {
    return Arrays.stream(DayOfWeek.values())
        .map(dayOfWeek -> mapToOpeningHoursPerDay(dayOfWeek, businessOpeningHoursSchedule.getScheduleOfDay(dayOfWeek)))
        .toList();
  }

  private OpeningHoursByDay mapToOpeningHoursPerDay(DayOfWeek dayOfWeek, List<Day> daysSchedule) {
    if (Objects.isNull(daysSchedule)) {
      return new OpeningHoursByDay(dayOfWeek, Collections.emptyList());
    }

    OpeningHoursByDay openingHoursByDay = new OpeningHoursByDay();
    List<Interval> intervals = daysSchedule.stream()
        .map(schedule -> new Interval(DateUtils.getHour(schedule.getStart()), DateUtils.getHour(schedule.getEnd())))
        .toList();
    openingHoursByDay.setDayOfWeek(dayOfWeek);
    openingHoursByDay.setInterval(intervals);

    return openingHoursByDay;
  }

  private Place createPlace(BusinessEntity businessEntity, List<OpeningHoursByDay> openingHoursByDays) {
    Place place = new Place();
    place.setName(businessEntity.getDisplayedWhat());
    place.setAddress(businessEntity.getDisplayedWhere());
    place.setOpeningHours(openingHoursByDays);

    return place;
  }
}
