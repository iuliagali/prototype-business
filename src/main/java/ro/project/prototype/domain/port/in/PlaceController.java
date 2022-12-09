package ro.project.prototype.domain.port.in;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.project.prototype.domain.model.Place;

@RestController
@RequestMapping("/place")
public interface PlaceController {

  @GetMapping("/{id}")
  @ApiOperation(value = "Get place by id.", nickname = "getPlaceById")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Success", response = Place.class),
      @ApiResponse(code = 404, message = "READ_PLACE_FAIL_NONEXISTENT"),
  })
  Place getPlace(@PathVariable String id);

}
