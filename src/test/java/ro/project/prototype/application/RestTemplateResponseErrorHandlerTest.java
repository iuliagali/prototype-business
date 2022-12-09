package ro.project.prototype.application;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ro.project.prototype.adapter.model.BusinessEntity;

@ExtendWith(SpringExtension.class)
@RestClientTest
@Import(value = {TestConfig.class})
public class RestTemplateResponseErrorHandlerTest {

  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private RestTemplate restTemplate;

  @BeforeEach
  public void init() {
    server = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  public void testHandleError_404NotFound() {
    Assertions.assertNotNull(this.restTemplate);
    Assertions.assertNotNull(this.server);

    this.server
        .expect(ExpectedCount.once(), requestTo("/test/123"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.NOT_FOUND));

    Assertions.assertThrows(ResourceNotFoundException.class, () -> {
      BusinessEntity response = restTemplate.getForObject("/test/123", BusinessEntity.class);
    });
  }

  @Test
  public void testHandleError_502BadGateway() {
    Assertions.assertNotNull(this.restTemplate);
    Assertions.assertNotNull(this.server);

    this.server
        .expect(ExpectedCount.once(), requestTo("/test/123"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.BAD_GATEWAY));

    Assertions.assertThrows(HttpServerErrorException.class, () -> {
      BusinessEntity response = restTemplate.getForObject("/test/123", BusinessEntity.class);
    });
  }
}
