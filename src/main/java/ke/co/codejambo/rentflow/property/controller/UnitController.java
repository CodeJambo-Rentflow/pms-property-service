package ke.co.codejambo.rentflow.property.controller;

import jakarta.validation.Valid;
import ke.co.codejambo.rentflow.property.dto.CreateUnitRequest;
import ke.co.codejambo.rentflow.property.dto.UnitResponse;
import ke.co.codejambo.rentflow.property.dto.VacantUnitResponse;
import ke.co.codejambo.rentflow.property.service.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UnitController {

    private final UnitService service;

    public UnitController(UnitService service) {
        this.service = service;
    }

    @PostMapping("/buildings/{buildingId}/units")
    @PreAuthorize("hasAuthority('SCOPE_property:write') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<UnitResponse> create(
            @PathVariable UUID buildingId,
            @Valid @RequestBody CreateUnitRequest req,
            @AuthenticationPrincipal Jwt jwt) {
        UUID actorId = null;
        try { actorId = UUID.fromString(jwt.getSubject()); } catch (Exception ignored) {}
        var created = service.create(buildingId, req, actorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(UnitResponse.from(created));
    }

    @GetMapping("/buildings/{buildingId}/units")
    @PreAuthorize("hasAuthority('SCOPE_property:read') or hasRole('SYSTEM_ADMIN')")
    public List<UnitResponse> listByBuilding(@PathVariable UUID buildingId) {
        return service.listByBuilding(buildingId).stream().map(UnitResponse::from).toList();
    }

    @GetMapping("/property-units/vacant")
    @PreAuthorize("hasAuthority('SCOPE_property:read') or hasRole('SYSTEM_ADMIN')")
    public List<VacantUnitResponse> listVacant() {
        return service.listVacant();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "success", false,
            "error", Map.of("code", "CONFLICT", "message", ex.getMessage())
        ));
    }
}
