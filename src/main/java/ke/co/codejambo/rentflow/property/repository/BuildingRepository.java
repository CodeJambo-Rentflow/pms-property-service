package ke.co.codejambo.rentflow.property.repository;

import ke.co.codejambo.rentflow.property.entity.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, UUID> {
    List<Building> findByLandlordIdAndDeletedAtIsNull(UUID landlordId);
    Page<Building> findByDeletedAtIsNull(Pageable pageable);
}
