DROP TABLE IF EXISTS alarm CASCADE;
DROP TABLE IF EXISTS organization CASCADE;
DROP TABLE IF EXISTS assist CASCADE;
DROP TABLE IF EXISTS control_resident CASCADE;
DROP TABLE IF EXISTS resident_emergency_contact CASCADE;
DROP TABLE IF EXISTS caregiver_password CASCADE;
DROP TABLE IF EXISTS resident CASCADE;
DROP TABLE IF EXISTS caregiver CASCADE;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE alarm (
    id UUID PRIMARY KEY,
    serial_numer VARCHAR(50) unique not null,
    count_number int
);

CREATE TABLE organization (
	id UUID primary key,
	alarm_id UUID not null,
	full_name VARCHAR(150) not null UNIQUE,
	cep VARCHAR(30),
	state VARCHAR(5),
	city VARCHAR(100),
	neighborhood VARCHAR(100),
	street VARCHAR(100),
	organization_number VARCHAR(50),
	
	FOREIGN KEY (alarm_id) REFERENCES alarm (id) ON DELETE CASCADE
);

CREATE TABLE caregiver  (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organization_id UUID not null,
    full_name VARCHAR(150) NOT null,
    active BOOLEAN NOT null DEFAULT FALSE,
    email VARCHAR(200) NOT null UNIQUE,
    image_data VARCHAR(512),
    phone VARCHAR(20) NOT null UNIQUE,
    cpf VARCHAR(11) NOT null UNIQUE,
    work_start_time  TIMESTAMP WITH TIME ZONE NOT null,
    work_end_time  TIMESTAMP WITH TIME ZONE NOT null,
        
    FOREIGN KEY (organization_id) REFERENCES organization (id) ON DELETE CASCADE
);

CREATE TABLE caregiver_password (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    caregiver_id UUID NOT null,
    caregiver_password VARCHAR(1024) NOT null,
    created_at TIMESTAMP WITH TIME ZONE NOT null,
    active BOOLEAN NOT null DEFAULT FALSE,
    staging BOOLEAN NOT null DEFAULT TRUE,
    verification_code VARCHAR(100) NOT null,
    code_valid_until TIMESTAMP WITH TIME ZONE NOT null,
        
    FOREIGN KEY (caregiver_id) REFERENCES caregiver (id) ON DELETE CASCADE,
    UNIQUE (caregiver_id, caregiver_password)
);

CREATE TABLE resident (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(150) NOT null,
    cpf VARCHAR(11) NOT null UNIQUE,
    sex CHAR(1) NOT null CHECK (sex IN ('M', 'F')),
    birth_date TIMESTAMP WITH TIME ZONE NOT null,
    created_at TIMESTAMP WITH TIME ZONE NOT null,
    updated_at TIMESTAMP WITH TIME ZONE NOT null,
    residential_unit VARCHAR(50) NOT null,
    image_data VARCHAR(512),

    UNIQUE (cpf, residential_unit)
);

CREATE TABLE resident_emergency_contact (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resident_id UUID NOT null,
    full_name VARCHAR(150) NOT null,
    phone VARCHAR(20) NOT null,
    relationship VARCHAR(50) NOT null,
        
    FOREIGN KEY (resident_id) REFERENCES resident (id) ON DELETE CASCADE,
    UNIQUE (resident_id, phone)
);

CREATE TABLE control_resident (
    id UUID PRIMARY KEY,
    control_id VARCHAR(32) NOT null,
    alarm_id UUID NOT null,
    resident_id UUID NOT null,
        
    FOREIGN KEY (resident_id) REFERENCES resident (id) ON DELETE CASCADE,
    FOREIGN KEY (alarm_id) REFERENCES alarm (id) ON DELETE cascade,
    UNIQUE (control_id, resident_id),
    UNIQUE (alarm_id, control_id)
);

CREATE TABLE assist (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    caregiver_id UUID,
    resident_id UUID NOT NULL,
    
    called_at TIMESTAMP WITH TIME ZONE NOT NULL,
    assignment_at TIMESTAMP WITH TIME ZONE,
    end_at TIMESTAMP WITH TIME ZONE,

    detail TEXT,
    severity_level VARCHAR NOT NULL CHECK (severity_level IN ('normal', 'emergency')),
    
    FOREIGN KEY (caregiver_id) REFERENCES caregiver (id) ON DELETE CASCADE,
    FOREIGN KEY (resident_id) REFERENCES resident (id) ON DELETE CASCADE,
    UNIQUE (caregiver_id, resident_id, called_at)
);

-- functions

CREATE OR REPLACE FUNCTION notify_assist()
RETURNS TRIGGER AS $$
DECLARE
    payload JSON;
BEGIN
    IF (TG_OP = 'DELETE') THEN
        payload := row_to_json(OLD);
    ELSE
        payload := row_to_json(NEW);
    END IF;

    PERFORM pg_notify(
        'caregiver_assignment_channel',
        json_build_object(
            'operation', TG_OP,
            'table', TG_TABLE_NAME,
            'data', payload
        )::text
    );

    IF (TG_OP = 'DELETE') THEN
        RETURN OLD;
    ELSE
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- triggers

CREATE TRIGGER trigger_notify_assist
AFTER INSERT OR UPDATE OR DELETE ON assist
FOR EACH ROW
EXECUTE FUNCTION notify_assist();
