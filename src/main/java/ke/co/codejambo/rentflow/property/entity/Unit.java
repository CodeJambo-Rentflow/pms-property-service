package ke.co.codejambo.rentflow.property.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "units", schema = "property")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "building_id", nullable = false)
    private UUID buildingId;

    @Column(name = "unit_number", nullable = false, length = 50)
    private String unitNumber;

    @Column(name = "unit_number_normalized", nullable = false, length = 50)
    private String unitNumberNormalized;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UnitType type;

    @Column(name = "monthly_rent", nullable = false)
    private Long monthlyRent;  // cents

    @Column(nullable = false, length = 3)
    private String currency = "KES";

    @Column(name = "deposit_months", nullable = false)
    private Short depositMonths = 2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UnitStatus status = UnitStatus.VACANT;

    @Column(name = "vacant_since")
    private OffsetDateTime vacantSince;

    private Short floor;

    private Short bedrooms;

    private Short bathrooms;

    @Column(name = "square_meters")
    private Integer squareMeters;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == UnitStatus.VACANT && this.vacantSince == null) {
            this.vacantSince = now;
        }
    }

    @PreUpdate
    void onUpdate() { this.updatedAt = OffsetDateTime.now(); }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getBuildingId() { return buildingId; }
    public void setBuildingId(UUID buildingId) { this.buildingId = buildingId; }
    public String getUnitNumber() { return unitNumber; }
    public void setUnitNumber(String unitNumber) { this.unitNumber = unitNumber; }
    public String getUnitNumberNormalized() { return unitNumberNormalized; }
    public void setUnitNumberNormalized(String unitNumberNormalized) { this.unitNumberNormalized = unitNumberNormalized; }
    public UnitType getType() { return type; }
    public void setType(UnitType type) { this.type = type; }
    public Long getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(Long monthlyRent) { this.monthlyRent = monthlyRent; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Short getDepositMonths() { return depositMonths; }
    public void setDepositMonths(Short depositMonths) { this.depositMonths = depositMonths; }
    public UnitStatus getStatus() { return status; }
    public void setStatus(UnitStatus status) { this.status = status; }
    public OffsetDateTime getVacantSince() { return vacantSince; }
    public void setVacantSince(OffsetDateTime vacantSince) { this.vacantSince = vacantSince; }
    public Short getFloor() { return floor; }
    public void setFloor(Short floor) { this.floor = floor; }
    public Short getBedrooms() { return bedrooms; }
    public void setBedrooms(Short bedrooms) { this.bedrooms = bedrooms; }
    public Short getBathrooms() { return bathrooms; }
    public void setBathrooms(Short bathrooms) { this.bathrooms = bathrooms; }
    public Integer getSquareMeters() { return squareMeters; }
    public void setSquareMeters(Integer squareMeters) { this.squareMeters = squareMeters; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(OffsetDateTime deletedAt) { this.deletedAt = deletedAt; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public UUID getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(UUID updatedBy) { this.updatedBy = updatedBy; }
}
