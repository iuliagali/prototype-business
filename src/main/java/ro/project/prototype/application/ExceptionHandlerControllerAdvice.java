package ro.project.prototype.application;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

  @ExceptionHandler(HttpServerErrorException.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionResponse handleHttpServerErrorException(HttpServerErrorException ex, HttpServletRequest request) {
    return buildAndLogExceptionResponse(ex.getMessage(), ex, request);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ExceptionResponse handleHttpNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
    return buildAndLogExceptionResponse(ex.getMessage(), ex, request);
  }

  @ExceptionHandler(HttpClientErrorException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleHttpClientErrorException(HttpClientErrorException ex, HttpServletRequest request) {
    return buildAndLogExceptionResponse(ex.getMessage(), ex, request);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionResponse handleAnyException(Exception ex, HttpServletRequest request) {
    return buildAndLogExceptionResponse("", ex, request);
  }

  protected ExceptionResponse buildAndLogExceptionResponse(String exceptionMessage, Exception ex,
      HttpServletRequest request) {
    ExceptionResponse exceptionResponse = buildExceptionResponse(request, exceptionMessage);
    logExceptionResponse(exceptionResponse, ex);
    return exceptionResponse;
  }

  private ExceptionResponse buildExceptionResponse(HttpServletRequest request, String exceptionMessage) {
    if (!ObjectUtils.isEmpty(exceptionMessage)) {
      return createExceptionResponse(request, exceptionMessage.trim());
    }
    return createExceptionResponse(request, "An unexpected internal error has occurred.");
  }

  private ExceptionResponse createExceptionResponse(HttpServletRequest request, String message) {
    ZoneId zid = ZoneId.systemDefault();
    ZonedDateTime datetime = ZonedDateTime.now(zid);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm:ss");
    String timestamp = datetime.format(formatter);

    return new ExceptionResponse(request.getRequestURI(), request.getMethod(), timestamp,
        message);
  }

  protected void logExceptionResponse(ExceptionResponse exceptionResponse, Exception ex) {
    LOGGER.error("Intercepted exception: {}. RequestUri: {}. HttpMethod: {}. Message: {}. ",
        ex.getClass().getSimpleName(), exceptionResponse.getRequestedUri(), exceptionResponse.getHttpMethod(),
        exceptionResponse.getMessage());
  }
}
