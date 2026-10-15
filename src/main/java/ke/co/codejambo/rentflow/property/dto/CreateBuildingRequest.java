package ke.co.codejambo.rentflow.property.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateBuildingRequest(
    @NotNull UUID landlordId,
    @NotBlank String name,
    @NotBlank String address,
    @NotBlank String county,
    String city,
    String postalCode,
    String description
) {}
