package ke.co.codejambo.rentflow.property.dto;

import ke.co.codejambo.rentflow.property.entity.Building;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BuildingResponse(
    UUID id,
    UUID landlordId,
    String name,
    String address,
    String county,
    String city,
    String postalCode,
    String description,
    Integer totalUnits,
    OffsetDateTime createdAt
) {
    public static BuildingResponse from(Building b) {
        return new BuildingResponse(
            b.getId(), b.getLandlordId(), b.getName(), b.getAddress(),
            b.getCounty(), b.getCity(), b.getPostalCode(), b.getDescription(),
            b.getTotalUnits(), b.getCreatedAt()
        );
    }
}
