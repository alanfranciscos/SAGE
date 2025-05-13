CREATE TABLE caregiver  (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        full_name VARCHAR(150) NOT NULL,
        active BOOLEAN NOT NULL DEFAULT FALSE,
        email VARCHAR(200) NOT NULL UNIQUE,
        image_data VARCHAR(512),
        phone VARCHAR(20)NOT NULL UNIQUE,
        cpf VARCHAR(11) NOT NULL UNIQUE,
        work_start_time  TIMESTAMP WITH TIME ZONE NOT NULL,
        work_end_time  TIMESTAMP WITH TIME ZONE NOT NULL
    );

CREATE TABLE caregiver_password (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        caregiver_id UUID NOT NULL,
        caregiver_password VARCHAR(1024) NOT NULL,
        created_at TIMESTAMP WITH TIME ZONE NOT NULL,
        active BOOLEAN NOT NULL DEFAULT FALSE,
        staging BOOLEAN NOT NULL DEFAULT TRUE,
        verification_code VARCHAR(100) NOT NULL,
        code_valid_until TIMESTAMP WITH TIME ZONE NOT NULL,
        
        FOREIGN KEY (caregiver_id) REFERENCES caregiver (id) ON DELETE CASCADE,
        UNIQUE (caregiver_id, caregiver_password)
    );

CREATE TABLE resident (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        full_name VARCHAR(150) NOT NULL,
        cpf VARCHAR(11) NOT NULL UNIQUE,
        sex CHAR(1) NOT NULL CHECK (sex IN ('M', 'F')),
        birth_date TIMESTAMP WITH TIME ZONE NOT NULL,
        created_at TIMESTAMP WITH TIME ZONE NOT NULL,
        updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
        residential_unit VARCHAR(50) NOT NULL,
        image_data VARCHAR(512),

        UNIQUE (cpf, residential_unit)
);

CREATE TABLE resident_emergency_contact (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        resident_id UUID NOT NULL,
        full_name VARCHAR(150) NOT NULL,
        phone VARCHAR(20) NOT NULL,
        relationship VARCHAR(50) NOT NULL,
        
        FOREIGN KEY (resident_id) REFERENCES resident (id) ON DELETE CASCADE,
        UNIQUE (resident_id, phone)
);

CREATE TABLE control_resident (
        id SERIAL PRIMARY KEY,
        control_id VARCHAR(32) NOT NULL UNIQUE,
        resident_id UUID NOT NULL,
        
        FOREIGN KEY (resident_id) REFERENCES resident (id) ON DELETE CASCADE,
        UNIQUE (control_id, resident_id)
);

CREATE TABLE caregiver_assignment_resident (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        caregiver_id UUID,
        resident_id UUID NOT NULL,
        
        called_at TIMESTAMP WITH TIME ZONE NOT NULL,
        assignment_at TIMESTAMP WITH TIME ZONE,
        end_at TIMESTAMP WITH TIME ZONE,

        -- 1 - Normal, 2 - Emergency
        detail TEXT,
        severity_level INTEGER NOT NULL CHECK (severity_level BETWEEN 1 AND 2),
        
        FOREIGN KEY (caregiver_id) REFERENCES caregiver (id) ON DELETE CASCADE,
        FOREIGN KEY (resident_id) REFERENCES resident (id) ON DELETE CASCADE,
        UNIQUE (caregiver_id, resident_id, called_at)
);

-- functions

CREATE OR REPLACE FUNCTION notify_caregiver_assignment_change()
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

CREATE TRIGGER trigger_notify_caregiver_assignment
AFTER INSERT OR UPDATE OR DELETE ON caregiver_assignment_resident
FOR EACH ROW
EXECUTE FUNCTION notify_caregiver_assignment_change();

-- inserts

-- Tabela caregiver
INSERT INTO caregiver (id, full_name, active, email, image_data, phone, cpf, work_start_time, work_end_time) VALUES
('5308c8bb-b882-42a6-8b1d-91d6cf216d71', 'Ana Souza', TRUE, 'ana.souza@gmail.com', 'https://image.com/ana.jpg', '999999999', '12345678901', '2025-05-01 08:00:00+00', '2025-05-01 17:00:00+00');
INSERT INTO caregiver (id, full_name, active, email, image_data, phone, cpf, work_start_time, work_end_time) VALUES
('59a62dd6-d19b-412e-91bb-df503b6ae3de', 'Carlos Pereira', TRUE, 'carlos.pereira@yahoo.com', 'https://image.com/carlos.jpg', '988888888', '23456789012', '2025-05-01 08:30:00+00', '2025-05-01 18:00:00+00');
INSERT INTO caregiver (id, full_name, active, email, image_data, phone, cpf, work_start_time, work_end_time) VALUES
('cc663ce3-0c6c-4808-acc3-f90c0ce63fe9', 'Maria Oliveira', FALSE, 'maria.oliveira@outlook.com', 'https://image.com/maria.jpg', '977777777', '34567890123', '2025-05-01 09:00:00+00', '2025-05-01 17:30:00+00');

-- Tabela caregiver_password
INSERT INTO caregiver_password (id, caregiver_id, caregiver_password, created_at, active, staging, verification_code, code_valid_until) VALUES
('2c342ef4-369f-4451-a50a-bf35a49a1bc5', '5308c8bb-b882-42a6-8b1d-91d6cf216d71', 'senha123456', '2025-05-01 09:00:00+00', TRUE, TRUE, 'abc123', '2025-05-02 00:00:00+00');
INSERT INTO caregiver_password (id, caregiver_id, caregiver_password, created_at, active, staging, verification_code, code_valid_until) VALUES
('c6e113e5-9a6c-43c0-bf3b-bd10b1dce2e4', '59a62dd6-d19b-412e-91bb-df503b6ae3de', 'senha987654', '2025-05-01 09:30:00+00', TRUE, FALSE, 'xyz789', '2025-05-02 00:00:00+00');
INSERT INTO caregiver_password (id, caregiver_id, caregiver_password, created_at, active, staging, verification_code, code_valid_until) VALUES
('a5a5d509-b0cb-4f65-b6e3-6a8f34c3f06d', 'cc663ce3-0c6c-4808-acc3-f90c0ce63fe9', 'senha654321', '2025-05-01 10:00:00+00', FALSE, TRUE, 'def456', '2025-05-02 00:00:00+00');

-- Tabela resident
INSERT INTO resident (id, full_name, cpf, sex, birth_date, created_at, updated_at, residential_unit, image_data) VALUES
('89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7', 'João Silva', '12345678901', 'M', '1990-07-15 00:00:00+00', '2025-05-01 10:00:00+00', '2025-05-01 10:30:00+00', 'A1', 'https://image.com/joao.jpg');
INSERT INTO resident (id, full_name, cpf, sex, birth_date, created_at, updated_at, residential_unit, image_data) VALUES
('416f4967-89b1-49f8-a8d3-134c6e63cf5b', 'Luana Costa', '98765432100', 'F', '1985-11-20 00:00:00+00', '2025-05-01 11:00:00+00', '2025-05-01 11:30:00+00', 'B2', 'https://image.com/luana.jpg');
INSERT INTO resident (id, full_name, cpf, sex, birth_date, created_at, updated_at, residential_unit, image_data) VALUES
('04d1f1c0-3022-4e53-9f34-d0136c89c9ed', 'Ricardo Gomes', '11223344556', 'M', '2000-03-10 00:00:00+00', '2025-05-01 12:00:00+00', '2025-05-01 12:30:00+00', 'C3', 'https://image.com/ricardo.jpg');

-- Tabela resident_emergency_contact
INSERT INTO resident_emergency_contact (id, resident_id, full_name, phone, relationship) VALUES
('0c21e5e3-1969-48e6-9fa2-6615c295140d', '89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7', 'Maria Silva', '988776655', 'Mother');
INSERT INTO resident_emergency_contact (id, resident_id, full_name, phone, relationship) VALUES
('6fbd7501-9bba-43eb-b3fe-66f6cf5c8b57', '416f4967-89b1-49f8-a8d3-134c6e63cf5b', 'José Costa', '977665544', 'Brother');
INSERT INTO resident_emergency_contact (id, resident_id, full_name, phone, relationship) VALUES
('679f9c38-e235-44df-a0df-c12d3915b171', '04d1f1c0-3022-4e53-9f34-d0136c89c9ed', 'Sandra Gomes', '966554433', 'Wife');

-- Inserts para a tabela control_resident
INSERT INTO control_resident (control_id, resident_id) VALUES
('001', '89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7');
INSERT INTO control_resident (control_id, resident_id) VALUES
('002', '416f4967-89b1-49f8-a8d3-134c6e63cf5b');
INSERT INTO control_resident (control_id, resident_id) VALUES
('003', '04d1f1c0-3022-4e53-9f34-d0136c89c9ed');

-- Tabela caregiver_assignment_resident
INSERT INTO caregiver_assignment_resident (id, caregiver_id, resident_id, called_at, assignment_at, end_at, detail, severity_level) VALUES
('de4be29a-09c6-4409-b03d-1a1b689004ed', '5308c8bb-b882-42a6-8b1d-91d6cf216d71', '89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7', '2025-05-01 08:30:00+00', '2025-05-01 09:00:00+00', '2025-05-01 18:00:00+00', null, 1);
INSERT INTO caregiver_assignment_resident (id, caregiver_id, resident_id, called_at, assignment_at, end_at, detail, severity_level) VALUES
('8f3a13d6-6c03-4cd3-89b8-5a285d983b30', '59a62dd6-d19b-412e-91bb-df503b6ae3de', '416f4967-89b1-49f8-a8d3-134c6e63cf5b', '2025-05-01 09:00:00+00', '2025-05-01 09:30:00+00', '2025-05-01 19:00:00+00', null, 2);
INSERT INTO caregiver_assignment_resident (id, caregiver_id, resident_id, called_at, assignment_at, end_at, detail, severity_level) VALUES
('6b0155fd-f7d3-4707-99a6-e86c1b29fe1c', 'cc663ce3-0c6c-4808-acc3-f90c0ce63fe9', '04d1f1c0-3022-4e53-9f34-d0136c89c9ed', '2025-05-01 10:00:00+00', '2025-05-01 10:30:00+00', '2025-05-01 20:00:00+00', null, 1);
