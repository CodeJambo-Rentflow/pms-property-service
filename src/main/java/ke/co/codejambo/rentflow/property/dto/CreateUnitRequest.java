package ke.co.codejambo.rentflow.property.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ke.co.codejambo.rentflow.property.entity.UnitType;

public record CreateUnitRequest(
    @NotBlank String unitNumber,
    @NotNull UnitType type,
    @NotNull @Min(1) Long monthlyRent,
    Short depositMonths,
    Short floor,
    Short bedrooms,
    Short bathrooms,
    Integer squareMeters,
    String notes
) {}
