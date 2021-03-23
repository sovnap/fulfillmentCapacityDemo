package com.yantriks.yso.capacity.cache.services.api.handler;

import com.yantriks.ypfp.ycs.location.services.core.dto.LocationAndFulfillmentTypeDetail;
import com.yantriks.ypfp.ycs.location.services.core.dto.key.LocationAndFulfillmentTypeDetailKey;
import com.yantriks.yso.capacity.cache.services.api.loader.LocationFulfillmentTypeEntryLoader;
import com.yantriks.yso.capacity.cache.services.core.dto.CapacityDetail;
import com.yantriks.yso.capacity.cache.services.core.dto.FulfillmentTypeCapacityDetail;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FulfillmentTypeCapacityExtensionHandler  {
  @Generated
  private static final Logger log = LoggerFactory.getLogger(FulfillmentTypeCapacityExtensionHandler.class);
  public FulfillmentTypeCapacityExtensionHandler() {
  }
  @Autowired
  private FulfillmentTypeCapacityHandler handler;

  @Autowired
  LocationFulfillmentTypeEntryLoader loader;



  public Mono<FulfillmentTypeCapacityDetail> upsert(FulfillmentTypeCapacityDetail fulfillmentTypeCapacityDetail) {
    LocationAndFulfillmentTypeDetailKey locationFTKey =
        LocationAndFulfillmentTypeDetailKey.builder().orgId(fulfillmentTypeCapacityDetail.getOrgId()).sellingChannel(fulfillmentTypeCapacityDetail.getSellingChannel())
            .locationType(fulfillmentTypeCapacityDetail.getLocationType()).locationId(fulfillmentTypeCapacityDetail.getLocationId())
            .fulfillmentType(fulfillmentTypeCapacityDetail.getFulfillmentType()).build();
    Mono<LocationAndFulfillmentTypeDetail> locationFTDetail = loader.getLoad(locationFTKey);

    log.debug("locationAndFulfillmentTypeDetail : {}", locationFTDetail.subscribe(System.out::println));
    locationFTDetail.subscribe(detail-> {
      loader.doLoad(buildLocationFTDetailRequest(fulfillmentTypeCapacityDetail.getCapacity(), detail)).subscribe();
      //log.debug("After update locationAndFulfillmentTypeDetail : {}", detail);
    });


        return handler.update(fulfillmentTypeCapacityDetail).doOnError(throwable -> {
      handler.create(fulfillmentTypeCapacityDetail);
    });
  }

  private Mono<LocationAndFulfillmentTypeDetail> buildLocationFTDetailRequest(Map<LocalDate, CapacityDetail> capacity,
      LocationAndFulfillmentTypeDetail detail) {
    if(capacity.containsKey(LocalDate.now())) {
      if (capacity.get(LocalDate.now()).getCapacity() > 0) {
        detail.setEnabled(true);
      } else if (capacity.get(LocalDate.now()).getCapacity() == 0) {
        detail.setEnabled(false);
      }
    }else{
      detail.setEnabled(false);
    }

    return Mono.just(detail);
  }

}
