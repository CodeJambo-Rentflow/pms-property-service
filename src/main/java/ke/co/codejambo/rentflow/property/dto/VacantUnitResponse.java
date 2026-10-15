package ke.co.codejambo.rentflow.property.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record VacantUnitResponse(
    UUID unitId,
    String unitNumber,
    UUID buildingId,
    String buildingName,
    Long monthlyRent,
    String currency,
    OffsetDateTime vacantSince,
    long daysVacant
) {}
