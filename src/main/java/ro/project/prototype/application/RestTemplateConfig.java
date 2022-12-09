package ro.project.prototype.application;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  private static final String GOOGLE_API_BASE_URL = "https://storage.googleapis.com";

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .rootUri(GOOGLE_API_BASE_URL)
        .errorHandler(new RestTemplateResponseErrorHandler())
        .build();  }
}
