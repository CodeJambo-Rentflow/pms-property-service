package ke.co.codejambo.rentflow.property.repository;

import ke.co.codejambo.rentflow.property.entity.Unit;
import ke.co.codejambo.rentflow.property.entity.UnitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {
    List<Unit> findByBuildingIdAndDeletedAtIsNull(UUID buildingId);
    List<Unit> findByStatusAndDeletedAtIsNull(UnitStatus status);
    Optional<Unit> findByBuildingIdAndUnitNumberNormalizedAndDeletedAtIsNull(UUID buildingId, String normalized);
    long countByBuildingIdAndDeletedAtIsNull(UUID buildingId);
}
