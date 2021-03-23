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
      loader.doLoad(buildLocationFTDetailRequest(fulfillmentTypeCapacityDetail.getCapacity(), locationFTDetail));
      log.debug("After update locationAndFulfillmentTypeDetail : {}", locationFTDetail.subscribe(System.out::println));
    });
      /*if(Boolean.FALSE.equals(hasElement))
        return loader.createLoad(Mono.just(LocationAndFulfillmentTypeDetail.builder()
                  .orgId(locationFTKey.getOrgId())
                  .sellingChannel(locationFTKey.getSellingChannel())
                  .locationType(locationFTKey.getLocationType())
                  .locationId(locationFTKey.getLocationId())
                  .fulfillmentType(locationFTKey.getFulfillmentType())
                  .enabled(false).build()));
      else

      return locationFTDetail;
    }).subscribe();*/
        return handler.update(fulfillmentTypeCapacityDetail).doOnError(throwable -> {
      handler.create(fulfillmentTypeCapacityDetail);
    });
  }

  private Mono<LocationAndFulfillmentTypeDetail> buildLocationFTDetailRequest(Map<LocalDate, CapacityDetail> capacity,
      Mono<LocationAndFulfillmentTypeDetail> locationFTDetail) {
    if(capacity.containsKey(LocalDate.now()) && capacity.get(LocalDate.now()).getCapacity() ==0) {
      locationFTDetail.flatMap(detail ->{
        if(detail.getEnabled())
          detail.setEnabled(false);
        return Mono.just(detail);
      });
    }else if(capacity.containsKey(LocalDate.now()) && capacity.get(LocalDate.now()).getCapacity() >0){
      locationFTDetail.flatMap(detail ->{
        if(!detail.getEnabled())
          detail.setEnabled(true);
        return Mono.just(detail);
      });
    }else{
      locationFTDetail.flatMap(detail ->{
        return Mono.just(detail);
      });
    }
    return locationFTDetail;
  }

}
