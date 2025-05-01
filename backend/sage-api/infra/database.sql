CREATE TABLE caregiver  (
        id SERIAL PRIMARY KEY,
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
        id SERIAL PRIMARY KEY,
        caregiver_id INTEGER NOT NULL,
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
        id SERIAL PRIMARY KEY,
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
        id SERIAL PRIMARY KEY,
        resident_id INTEGER NOT NULL,
        full_name VARCHAR(150) NOT NULL,
        phone VARCHAR(20) NOT NULL,
        relationship VARCHAR(50) NOT NULL,
        
        FOREIGN KEY (resident_id) REFERENCES resident (id) ON DELETE CASCADE,
        UNIQUE (resident_id, phone)
);

CREATE TABLE control_resident (
        id SERIAL PRIMARY KEY,
        control_id INTEGER NOT NULL UNIQUE,
        resident_id INTEGER NOT NULL,
        
        FOREIGN KEY (resident_id) REFERENCES resident (id) ON DELETE CASCADE,
        UNIQUE (control_id, resident_id)
);

CREATE TABLE caregiver_assignment_resident (
        id SERIAL PRIMARY KEY,
        caregiver_id INTEGER NOT NULL,
        resident_id INTEGER NOT NULL,
        
        called_at TIMESTAMP WITH TIME ZONE NOT NULL,
        assignment_at TIMESTAMP WITH TIME ZONE NOT NULL,
        end_date TIMESTAMP WITH TIME ZONE NOT NULL,
        
        FOREIGN KEY (caregiver_id) REFERENCES caregiver (id) ON DELETE CASCADE,
        FOREIGN KEY (resident_id) REFERENCES resident (id) ON DELETE CASCADE,
        UNIQUE (caregiver_id, resident_id, called_at)
);