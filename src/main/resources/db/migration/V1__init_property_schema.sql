-- V1__init_property_schema.sql
-- Property service initial schema per PMS-DB-001 §5

CREATE SCHEMA IF NOT EXISTS property;

CREATE TABLE property.buildings (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    landlord_id     UUID NOT NULL,
    name            VARCHAR(255) NOT NULL,
    address         TEXT NOT NULL,
    county          VARCHAR(50) NOT NULL,
    city            VARCHAR(100),
    postal_code     VARCHAR(20),
    description     TEXT,
    total_units     INTEGER NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      UUID,
    updated_by      UUID,
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX ix_buildings_landlord ON property.buildings (landlord_id) WHERE deleted_at IS NULL;
CREATE INDEX ix_buildings_county ON property.buildings (county) WHERE deleted_at IS NULL;

CREATE TABLE property.units (
    id                        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    building_id               UUID NOT NULL REFERENCES property.buildings(id) ON DELETE RESTRICT,
    unit_number               VARCHAR(50) NOT NULL,
    unit_number_normalized    VARCHAR(50) NOT NULL,
    type                      VARCHAR(20) NOT NULL,
    monthly_rent              BIGINT NOT NULL CHECK (monthly_rent > 0),
    currency                  VARCHAR(3) NOT NULL DEFAULT 'KES',
    deposit_months            SMALLINT NOT NULL DEFAULT 2 CHECK (deposit_months BETWEEN 0 AND 6),
    status                    VARCHAR(20) NOT NULL DEFAULT 'VACANT',
    vacant_since              TIMESTAMPTZ,
    floor                     SMALLINT,
    bedrooms                  SMALLINT,
    bathrooms                 SMALLINT,
    square_meters             INTEGER,
    notes                     TEXT,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by                UUID,
    updated_by                UUID,
    deleted_at                TIMESTAMPTZ,
    CONSTRAINT ux_units_building_number UNIQUE (building_id, unit_number_normalized)
);

CREATE INDEX ix_units_building ON property.units (building_id) WHERE deleted_at IS NULL;
CREATE INDEX ix_units_status ON property.units (status) WHERE deleted_at IS NULL;
CREATE INDEX ix_units_vacant ON property.units (vacant_since) WHERE status = 'VACANT';
