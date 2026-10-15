package ke.co.codejambo.rentflow.property.service;

import ke.co.codejambo.rentflow.property.dto.BuildingResponse;
import ke.co.codejambo.rentflow.property.dto.CreateBuildingRequest;
import ke.co.codejambo.rentflow.property.entity.Building;
import ke.co.codejambo.rentflow.property.repository.BuildingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BuildingService {

    private final BuildingRepository repository;

    public BuildingService(BuildingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Building create(CreateBuildingRequest req, UUID actorId) {
        Building b = new Building();
        b.setLandlordId(req.landlordId());
        b.setName(req.name());
        b.setAddress(req.address());
        b.setCounty(req.county());
        b.setCity(req.city());
        b.setPostalCode(req.postalCode());
        b.setDescription(req.description());
        b.setCreatedBy(actorId);
        b.setUpdatedBy(actorId);
        return repository.save(b);
    }

    @Transactional(readOnly = true)
    public Building getById(UUID id) {
        return repository.findById(id)
            .filter(b -> b.getDeletedAt() == null)
            .orElseThrow(() -> new IllegalStateException("Building not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Building> listByLandlord(UUID landlordId) {
        return repository.findByLandlordIdAndDeletedAtIsNull(landlordId);
    }

    @Transactional(readOnly = true)
    public List<Building> listAll() {
        return repository.findByDeletedAtIsNull(org.springframework.data.domain.Pageable.unpaged()).getContent();
    }
}
