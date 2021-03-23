package com.yantriks.yso.capacity.cache.services.api.loader;

import com.yantriks.ypfp.common.api.exception.EntityDoesNotExistException;
import com.yantriks.ypfp.ycs.location.services.core.dto.LocationAndFulfillmentTypeDetail;
import com.yantriks.ypfp.ycs.location.services.core.dto.key.LocationAndFulfillmentTypeDetailKey;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LocationFulfillmentTypeEntryLoader {

  @Autowired
  private WebClient webClient;


  @Value("${common.services.locationFulfillmentType-api.url}")
  private String locationFulfillmentTypeUrl;

  public Mono<LocationAndFulfillmentTypeDetail> getLoad(LocationAndFulfillmentTypeDetailKey key)  {

        return webClient
            .method(HttpMethod.GET)
            .uri(locationFulfillmentTypeUrl + getUriComponents(key))
            .retrieve().bodyToMono(LocationAndFulfillmentTypeDetail.class);



  }

  public Mono<? extends LocationAndFulfillmentTypeDetail> createLoad(Mono<LocationAndFulfillmentTypeDetail> detail) {

    return webClient
        .method(HttpMethod.PUT)
        .uri(locationFulfillmentTypeUrl + "/location-services/location-fulfillment-type")
        .body(detail, LocationAndFulfillmentTypeDetail.class)
        .retrieve()
        .bodyToMono(LocationAndFulfillmentTypeDetail.class);
  }

  public Mono<LocationAndFulfillmentTypeDetail> doLoad(Mono<LocationAndFulfillmentTypeDetail> detail)  {
      return webClient
          .method(HttpMethod.PUT)
          .uri(locationFulfillmentTypeUrl + "/location-services/location-fulfillment-type")
          .body(detail, LocationAndFulfillmentTypeDetail.class)
          .retrieve()
          .bodyToMono(LocationAndFulfillmentTypeDetail.class);
  }
  private UriComponents getUriComponents(LocationAndFulfillmentTypeDetailKey key) {
    return UriComponentsBuilder.fromUriString("/common-services/location")
        .path("/{orgId}")
        .path("/{sellingChannel}")
        .path("/{locationType}")
        .path("/{locationId}")
        .path("/{fulfillmentType}")
        .buildAndExpand(key.getOrgId(), key.getSellingChannel(), key.getLocationType(), key.getLocationId(),
            key.getFulfillmentType());
  }
  protected Logger log() {
    return log;
  }

}
