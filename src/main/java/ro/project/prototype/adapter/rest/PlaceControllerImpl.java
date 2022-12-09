package ro.project.prototype.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.project.prototype.domain.model.Place;
import ro.project.prototype.domain.port.in.PlaceController;
import ro.project.prototype.domain.service.PlaceService;

@Component
public class PlaceControllerImpl implements PlaceController {

  private final PlaceService placeService;

  @Autowired
  public PlaceControllerImpl(PlaceService placeService) {
    this.placeService = placeService;
  }

  @Override
  public Place getPlace(String id) {
    return placeService.getPlaceById(id);
  }
}
