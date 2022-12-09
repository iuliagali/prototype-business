package ro.project.prototype.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

  private String requestedUri;
  private String httpMethod;
  private String timestamp;
  private String message;
}
