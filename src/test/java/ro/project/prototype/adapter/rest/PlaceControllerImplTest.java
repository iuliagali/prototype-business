package ro.project.prototype.adapter.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.project.prototype.domain.model.Place;
import ro.project.prototype.domain.service.PlaceService;

@ExtendWith(MockitoExtension.class)
public class PlaceControllerImplTest {

  @InjectMocks
  private PlaceControllerImpl placeController;

  @Mock
  private PlaceService placeService;

  @Test
  public void testGetPlace_ok() {
    Place place = Place.builder().name("name").address("address").openingHours(
        Collections.emptyList()).build();
    Mockito.when(placeService.getPlaceById(eq("id"))).thenReturn(place);

    Place result = placeController.getPlace("id");
    assertEquals(place, result);
  }

  @Test
  public void testGetPlace_null() {
    Mockito.when(placeService.getPlaceById(eq("id"))).thenReturn(null);

    Place result = placeController.getPlace("id");
    assertNull(result);
  }
}
