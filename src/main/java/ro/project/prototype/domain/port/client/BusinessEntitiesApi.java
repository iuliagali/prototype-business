package ro.project.prototype.domain.port.client;

import org.springframework.stereotype.Component;
import ro.project.prototype.adapter.model.BusinessEntity;

@Component
public interface BusinessEntitiesApi {

  BusinessEntity getBusinessById(String id);
}
