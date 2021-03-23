package com.yantriks.yso.capacity.cache.services.api.dto.request;

import com.yantriks.ypfp.common.api.dto.AuditCrudRequest;
import com.yantriks.ypfp.common.api.dto.GenericSwaggerDocs.CommonFields;
import com.yantriks.yso.capacity.cache.services.api.dto.SwaggerDocs;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@SuperBuilder(toBuilder = true)
public class FulfillmentTypeCapacityDetailRequest extends AuditCrudRequest {

  @Schema(required = true, example = CommonFields.ORG_ID_EXAMPLE, description = CommonFields.ORG_ID_NOTES)
  @NotBlank
  private String orgId;

  @Schema(required = true, example = CommonFields.SELLING_CHANNEL_EXAMPLE, description = CommonFields.SELLING_CHANNEL_NOTES)
  @NotBlank
  private String sellingChannel;

  @Schema(required = true, example = CommonFields.LOCATION_TYPE_EXAMPLE, description = CommonFields.LOCATION_TYPE_NOTES)
  @NotBlank
  private String locationType;

  @Schema(required = true, example = CommonFields.LOCATION_ID_EXAMPLE, description = CommonFields.LOCATION_ID_NOTES)
  @NotBlank
  private String locationId;

  @Schema(required = true, example = CommonFields.FULFILLMENT_TYPE_EXAMPLE, description = CommonFields.FULFILLMENT_TYPE_NOTES)
  @NotBlank
  private String fulfillmentType;

  @Schema(required = true, example = CommonFields.CAPACITY_SEGMENT_EXAMPLE, description = CommonFields.CAPACITY_SEGMENT_NOTES)
  @NotBlank
  private String capacitySegment;

  @Schema(required = true, example = SwaggerDocs.CAPACITY_MAP_EXAMPLE, description = CommonFields.CAPACITY_NOTES)
  @NotEmpty
  private Map<LocalDate, @NotNull @Valid CapacityDetailRequest> capacity;

  @Schema(required = true, example = CommonFields.UPDATED_TIME_EXAMPLE, description = CommonFields.UPDATED_TIME_NOTES)
  private ZonedDateTime updateTime;

}

