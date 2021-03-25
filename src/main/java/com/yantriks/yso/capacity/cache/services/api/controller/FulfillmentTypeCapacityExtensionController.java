package com.yantriks.yso.capacity.cache.services.api.controller;

import com.yantriks.ypfp.common.api.controller.GenericController;
import com.yantriks.yso.capacity.cache.services.api.controller.SwaggerDocs.CapacityCacheServices;
import com.yantriks.yso.capacity.cache.services.api.dto.request.FulfillmentTypeCapacityDetailRequest;
import com.yantriks.yso.capacity.cache.services.api.dto.response.FulfillmentTypeCapacityDetailResponse;
import com.yantriks.yso.capacity.cache.services.api.handler.FulfillmentTypeCapacityExtensionHandler;
import com.yantriks.yso.capacity.cache.services.api.handler.FulfillmentTypeCapacityHandler;
import com.yantriks.yso.capacity.cache.services.api.mapper.FulfillmentTypeCapacityDetailAndRequestResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@Profile("!hazelcast-cluster")
@Tag(name = "1.2 - Cache Capacity Services", description = "REST API for fulfillment-type-capacity.")
@RequestMapping(value = FulfillmentTypeCapacityExtensionController.ENDPOINT)
public class FulfillmentTypeCapacityExtensionController extends GenericController<FulfillmentTypeCapacityDetailRequest, FulfillmentTypeCapacityDetailResponse> {
  public static final String ENDPOINT = "/capacity-cache-services/extension/fulfillment-type-capacity";

  @Autowired
  private FulfillmentTypeCapacityExtensionHandler handler;

  @Autowired
  private FulfillmentTypeCapacityDetailAndRequestResponseMapper mapper;

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Disable/Enable Location Fulfillment Type data as per Capacity Cache Data", description = CapacityCacheServices.FULFILLMENT_TYPE_CAPACITY_POST_API_NOTES)
  public Mono<ResponseEntity<FulfillmentTypeCapacityDetailResponse>> upsert(
      @Parameter(required = true) @Valid @RequestBody FulfillmentTypeCapacityDetailRequest request) {

    return handler.upsert(mapper.requestToDetail(request))
        .map(mapper::toResponse)
        .transform(processGenericUpdateResponse(request));

  }

  protected Logger log() {
    return log;
  }

  protected String getMetricURI() {
    return "/capacity-cache-services/extension/fulfillment-type-capacity";
  }
}
