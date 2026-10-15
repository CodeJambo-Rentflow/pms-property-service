package ke.co.codejambo.rentflow.property.service;

import ke.co.codejambo.rentflow.property.dto.CreateUnitRequest;
import ke.co.codejambo.rentflow.property.dto.VacantUnitResponse;
import ke.co.codejambo.rentflow.property.entity.Unit;
import ke.co.codejambo.rentflow.property.entity.UnitStatus;
import ke.co.codejambo.rentflow.property.repository.BuildingRepository;
import ke.co.codejambo.rentflow.property.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final BuildingRepository buildingRepository;

    public UnitService(UnitRepository unitRepository, BuildingRepository buildingRepository) {
        this.unitRepository = unitRepository;
        this.buildingRepository = buildingRepository;
    }

    @Transactional
    public Unit create(UUID buildingId, CreateUnitRequest req, UUID actorId) {
        // Building must exist
        var building = buildingRepository.findById(buildingId)
            .filter(b -> b.getDeletedAt() == null)
            .orElseThrow(() -> new IllegalStateException("Building not found: " + buildingId));

        String normalized = normalize(req.unitNumber());
        if (unitRepository.findByBuildingIdAndUnitNumberNormalizedAndDeletedAtIsNull(buildingId, normalized).isPresent()) {
            throw new IllegalStateException("Unit number already exists in this building: " + req.unitNumber());
        }

        Unit u = new Unit();
        u.setBuildingId(buildingId);
        u.setUnitNumber(req.unitNumber());
        u.setUnitNumberNormalized(normalized);
        u.setType(req.type());
        u.setMonthlyRent(req.monthlyRent());
        if (req.depositMonths() != null) u.setDepositMonths(req.depositMonths());
        u.setFloor(req.floor());
        u.setBedrooms(req.bedrooms());
        u.setBathrooms(req.bathrooms());
        u.setSquareMeters(req.squareMeters());
        u.setNotes(req.notes());
        u.setStatus(UnitStatus.VACANT);
        u.setVacantSince(OffsetDateTime.now());
        u.setCreatedBy(actorId);
        u.setUpdatedBy(actorId);

        Unit saved = unitRepository.save(u);

        // Update building's total_units counter
        building.setTotalUnits((int) unitRepository.countByBuildingIdAndDeletedAtIsNull(buildingId));
        buildingRepository.save(building);

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Unit> listByBuilding(UUID buildingId) {
        return unitRepository.findByBuildingIdAndDeletedAtIsNull(buildingId);
    }

    @Transactional(readOnly = true)
    public List<VacantUnitResponse> listVacant() {
        return unitRepository.findByStatusAndDeletedAtIsNull(UnitStatus.VACANT).stream()
            .map(u -> {
                var building = buildingRepository.findById(u.getBuildingId()).orElse(null);
                String buildingName = building != null ? building.getName() : "Unknown";
                long daysVacant = u.getVacantSince() == null ? 0 :
                    Duration.between(u.getVacantSince(), OffsetDateTime.now()).toDays();
                return new VacantUnitResponse(
                    u.getId(), u.getUnitNumber(), u.getBuildingId(),
                    buildingName, u.getMonthlyRent(), u.getCurrency(),
                    u.getVacantSince(), daysVacant
                );
            })
            .toList();
    }

    private static String normalize(String input) {
        if (input == null) return null;
        String s = input.toUpperCase().trim()
            .replaceAll("\\s+", "")
            .replaceAll("^(UNIT|APT|HOUSE|NO)", "")
            .replaceAll("[^A-Z0-9]", "");
        // Strip leading zeros from trailing numeric suffix
        return s.replaceFirst("0+(\\d+)$", "$1");
    }
}
