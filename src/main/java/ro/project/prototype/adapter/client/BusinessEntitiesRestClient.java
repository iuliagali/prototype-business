package ro.project.prototype.adapter.client;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ro.project.prototype.adapter.model.BusinessEntity;
import ro.project.prototype.domain.port.client.BusinessEntitiesApi;

@Component
public class BusinessEntitiesRestClient implements BusinessEntitiesApi {

  private static final String PLACE_BY_ID = "/coding-session-rest-api/{%s}";

  private static final ParameterizedTypeReference<BusinessEntity> rootResponseType = new ParameterizedTypeReference<>() {
  };

  private final RestTemplate restTemplate;

  @Autowired
  public BusinessEntitiesRestClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public BusinessEntity getBusinessById(String id) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    URI uri = UriComponentsBuilder.fromPath(PLACE_BY_ID).build(id);
    HttpEntity<BusinessEntity> response = restTemplate.exchange(
        uri.toString(),
        HttpMethod.GET,
        new HttpEntity<>(headers),
        rootResponseType
    );

    return response.getBody();
  }
}
