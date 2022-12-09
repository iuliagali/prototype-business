package ro.project.prototype.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return response.getStatusCode().series() == Series.CLIENT_ERROR
        || response.getStatusCode().series() == Series.SERVER_ERROR;
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
  }

  @Override
  public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
    LOGGER.error("A {} error was intercepted while calling the following API: {} {}.", response.getRawStatusCode(),
        method.name(), url);
    LOGGER.error("Error message: {} {}", response.getStatusText(), this.readResponseMessage(response.getBody()));

    if (response.getStatusCode().is4xxClientError()) {
      if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
        throw new ResourceNotFoundException("Resource not found.");
      }
      throw new HttpClientErrorException(response.getStatusCode(), response.getStatusText());
    } else {
      throw new HttpServerErrorException(response.getStatusCode(), response.getStatusText());
    }
  }

  private String readResponseMessage(InputStream inputStream) {
    return (new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))).lines().collect(
        Collectors.joining("\n"));
  }
}
