package ro.project.prototype.adapter.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ro.project.prototype.adapter.model.BusinessEntity;

@ExtendWith(MockitoExtension.class)
public class BusinessEntitiesRestClientTest {

  @InjectMocks
  BusinessEntitiesRestClient underTest;

  @Mock
  RestTemplate restTemplate;

  @Test
  public void testGetBusinessById_ok() {
    ResponseEntity<BusinessEntity> businessEntityHttpEntity = mock(ResponseEntity.class);
    when(businessEntityHttpEntity.getBody()).thenReturn(
        BusinessEntity.builder().displayedWhat("name").displayedWhere("address").businessSchedule(null).build());
    when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class),
        any(ParameterizedTypeReference.class))).thenReturn(businessEntityHttpEntity);

    BusinessEntity entity = underTest.getBusinessById("id");

    assertNotNull(entity);
  }

}
