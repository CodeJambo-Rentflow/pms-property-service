package ke.co.codejambo.rentflow.property.controller;

import jakarta.validation.Valid;
import ke.co.codejambo.rentflow.property.dto.BuildingResponse;
import ke.co.codejambo.rentflow.property.dto.CreateBuildingRequest;
import ke.co.codejambo.rentflow.property.service.BuildingService;
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
@RequestMapping("/api/v1/buildings")
public class BuildingController {

    private final BuildingService service;

    public BuildingController(BuildingService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_property:write') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<BuildingResponse> create(
            @Valid @RequestBody CreateBuildingRequest req,
            @AuthenticationPrincipal Jwt jwt) {
        UUID actorId = null;
        try { actorId = UUID.fromString(jwt.getSubject()); } catch (Exception ignored) {}
        var created = service.create(req, actorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(BuildingResponse.from(created));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_property:read') or hasRole('SYSTEM_ADMIN')")
    public List<BuildingResponse> list() {
        return service.listAll().stream().map(BuildingResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_property:read') or hasRole('SYSTEM_ADMIN')")
    public BuildingResponse getById(@PathVariable UUID id) {
        return BuildingResponse.from(service.getById(id));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "success", false,
            "error", Map.of("code", "CONFLICT", "message", ex.getMessage())
        ));
    }
}
