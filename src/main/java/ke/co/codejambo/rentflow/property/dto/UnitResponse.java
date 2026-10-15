package ke.co.codejambo.rentflow.property.dto;

import ke.co.codejambo.rentflow.property.entity.Unit;
import ke.co.codejambo.rentflow.property.entity.UnitStatus;
import ke.co.codejambo.rentflow.property.entity.UnitType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UnitResponse(
    UUID id,
    UUID buildingId,
    String unitNumber,
    UnitType type,
    Long monthlyRent,
    String currency,
    Short depositMonths,
    UnitStatus status,
    OffsetDateTime vacantSince,
    Short floor,
    Short bedrooms,
    Short bathrooms,
    Integer squareMeters,
    String notes,
    OffsetDateTime createdAt
) {
    public static UnitResponse from(Unit u) {
        return new UnitResponse(
            u.getId(), u.getBuildingId(), u.getUnitNumber(), u.getType(),
            u.getMonthlyRent(), u.getCurrency(), u.getDepositMonths(), u.getStatus(),
            u.getVacantSince(), u.getFloor(), u.getBedrooms(), u.getBathrooms(),
            u.getSquareMeters(), u.getNotes(), u.getCreatedAt()
        );
    }
}
