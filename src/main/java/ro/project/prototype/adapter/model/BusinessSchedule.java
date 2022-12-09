package ro.project.prototype.adapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BusinessSchedule {

  @JsonProperty("days")
  public OpeningHours hours;
  @JsonProperty("closed_on_holidays")
  public boolean closedOnHolidays;
  @JsonProperty("open_by_arrangement")
  public boolean openByArrangement;
}
