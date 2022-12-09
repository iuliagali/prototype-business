package ro.project.prototype.adapter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessEntity {

  @JsonProperty("displayed_what")
  public String displayedWhat;
  @JsonProperty("displayed_where")
  public String displayedWhere;
  @JsonProperty("opening_hours")
  public BusinessSchedule businessSchedule;
}
