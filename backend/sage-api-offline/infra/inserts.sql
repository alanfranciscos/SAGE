-- Inserts para todas as tabelas

-- Tabela alarm
INSERT INTO alarm (id, serial_numer, count_number) VALUES
('a1b2c3d4-e5f6-4789-a012-3456789abcde', 'ALM001', 0),
('b2c3d4e5-f6a7-4890-b123-456789abcdef', 'ALM002', 0),
('c3d4e5f6-a7b8-4901-c234-56789abcdef0', 'ALM003', 0);

-- Tabela organization
INSERT INTO organization (id, alarm_id, full_name, cep, state, city, neighborhood, street, organization_number) VALUES
('d4e5f6a7-b8c9-4012-d345-6789abcdef01', 'a1b2c3d4-e5f6-4789-a012-3456789abcde', 'Casa de Repouso Esperança', '12345-678', 'SP', 'São Paulo', 'Vila Mariana', 'Rua das Flores', '123'),
('e5f6a7b8-c9d0-4123-e456-789abcdef012', 'b2c3d4e5-f6a7-4890-b123-456789abcdef', 'Lar dos Idosos São José', '23456-789', 'RJ', 'Rio de Janeiro', 'Copacabana', 'Avenida Atlântica', '456'),
('f6a7b8c9-d0e1-4234-f567-89abcdef0123', 'c3d4e5f6-a7b8-4901-c234-56789abcdef0', 'Centro de Cuidados Vida Plena', '34567-890', 'MG', 'Belo Horizonte', 'Savassi', 'Rua da Paz', '789');

-- Tabela caregiver
INSERT INTO caregiver (id, organization_id, full_name, active, email, image_data, phone, cpf, work_start_time, work_end_time) VALUES
('11111111-2222-4333-a444-555666777888', 'd4e5f6a7-b8c9-4012-d345-6789abcdef01', 'Maria Santos', true, 'maria.santos@email.com', 'https://image.com/maria.jpg', '11987654321', '12345678900', '2025-01-01 08:00:00+00', '2025-01-01 18:00:00+00'),
('22222222-3333-4444-b555-666777888999', 'e5f6a7b8-c9d0-4123-e456-789abcdef012', 'Carlos Oliveira', true, 'carlos.oliveira@email.com', 'https://image.com/carlos.jpg', '21987654321', '23456789011', '2025-01-01 06:00:00+00', '2025-01-01 14:00:00+00'),
('33333333-4444-5555-c666-777888999000', 'f6a7b8c9-d0e1-4234-f567-89abcdef0123', 'Ana Lima', true, 'ana.lima@email.com', 'https://image.com/ana.jpg', '31987654321', '34567890122', '2025-01-01 14:00:00+00', '2025-01-01 22:00:00+00'),
('44444444-5555-6666-d777-888999000111', 'd4e5f6a7-b8c9-4012-d345-6789abcdef01', 'Roberto Silva', true, 'roberto.silva@email.com', 'https://image.com/roberto.jpg', '11976543210', '45678901233', '2025-01-01 22:00:00+00', '2025-01-02 06:00:00+00');

-- Tabela caregiver_password
INSERT INTO caregiver_password (id, caregiver_id, caregiver_password, created_at, active, staging, verification_code, code_valid_until) VALUES
('aaaa1111-bbbb-4ccc-dddd-eeeefffff000', '11111111-2222-4333-a444-555666777888', '$2a$10$N9qo8uLOickgx2ZMRZoMye.TjKd3QsYsJYWbSSVGJjj4a4gJ5OsW6', '2025-01-01 10:00:00+00', true, false, 'VERIFY123', '2025-01-02 10:00:00+00'),
('bbbb2222-cccc-4ddd-eeee-fffff0000111', '22222222-3333-4444-b555-666777888999', '$2a$10$N9qo8uLOickgx2ZMRZoMye.TjKd3QsYsJYWbSSVGJjj4a4gJ5OsW6', '2025-01-01 11:00:00+00', true, false, 'VERIFY456', '2025-01-02 11:00:00+00'),
('cccc3333-dddd-4eee-ffff-000011112222', '33333333-4444-5555-c666-777888999000', '$2a$10$N9qo8uLOickgx2ZMRZoMye.TjKd3QsYsJYWbSSVGJjj4a4gJ5OsW6', '2025-01-01 12:00:00+00', true, false, 'VERIFY789', '2025-01-02 12:00:00+00'),
('dddd4444-eeee-4fff-0000-111122223333', '44444444-5555-6666-d777-888999000111', '$2a$10$N9qo8uLOickgx2ZMRZoMye.TjKd3QsYsJYWbSSVGJjj4a4gJ5OsW6', '2025-01-01 13:00:00+00', true, false, 'VERIFY012', '2025-01-02 13:00:00+00');

-- Tabela resident
INSERT INTO resident (id, full_name, cpf, sex, birth_date, created_at, updated_at, residential_unit, image_data, active) VALUES
('89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7', 'João Silva', '12345678901', 'M', '1950-07-15 00:00:00+00', '2025-05-01 10:00:00+00', '2025-05-01 10:30:00+00', 'A1', 'https://image.com/joao.jpg', true),
('416f4967-89b1-49f8-a8d3-134c6e63cf5b', 'Luana Costa', '98765432100', 'F', '1945-11-20 00:00:00+00', '2025-05-01 11:00:00+00', '2025-05-01 11:30:00+00', 'B2', 'https://image.com/luana.jpg', true),
('04d1f1c0-3022-4e53-9f34-d0136c89c9ed', 'Ricardo Gomes', '11223344556', 'M', '1955-03-10 00:00:00+00', '2025-05-01 12:00:00+00', '2025-05-01 12:30:00+00', 'C3', 'https://image.com/ricardo.jpg', true),
('55555555-6666-4777-8888-999000111222', 'Helena Fernandes', '55566677788', 'F', '1948-12-05 00:00:00+00', '2025-05-01 13:00:00+00', '2025-05-01 13:30:00+00', 'D4', 'https://image.com/helena.jpg', true),
('66666666-7777-4888-9999-000111222333', 'Pedro Almeida', '66677788899', 'M', '1952-08-25 00:00:00+00', '2025-05-01 14:00:00+00', '2025-05-01 14:30:00+00', 'E5', 'https://image.com/pedro.jpg', true),
('99999999-7777-4888-9999-000111222333', 'Teste normal', '66677788891', 'M', '1952-08-25 00:00:00+00', '2025-05-01 14:00:00+00', '2025-05-01 14:30:00+00', 'E5', 'https://image.com/pedro.jpg', true),
('99998888-7777-4888-9999-000111222333', 'Teste warning', '66677785891', 'M', '1952-08-25 00:00:00+00', '2025-05-01 14:00:00+00', '2025-05-01 14:30:00+00', 'E5', 'https://image.com/pedro.jpg', true);

-- Tabela resident_emergency_contact
INSERT INTO resident_emergency_contact (id, resident_id, full_name, phone, relationship) VALUES
('eee11111-fff2-4222-3333-444455556666', '89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7', 'Maria Silva', '11987651234', 'Filha'),
('fff22222-aaa3-4333-4444-555566667777', '89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7', 'José Silva', '11987651235', 'Filho'),
('aaa33333-bbb4-4444-5555-666677778888', '416f4967-89b1-49f8-a8d3-134c6e63cf5b', 'Carlos Costa', '21987651236', 'Sobrinho'),
('bbb44444-ccc5-4555-6666-777788889999', '04d1f1c0-3022-4e53-9f34-d0136c89c9ed', 'Ana Gomes', '31987651237', 'Neta'),
('ccc55555-ddd6-4666-7777-888899990000', '55555555-6666-4777-8888-999000111222', 'Roberto Fernandes', '11987651238', 'Neto'),
('ddd66666-eee7-4777-8888-999900001111', '66666666-7777-4888-9999-000111222333', 'Sandra Almeida', '21987651239', 'Filha');

-- Tabela control_resident
INSERT INTO control_resident (id, control_id, alarm_id, resident_id) VALUES
('77777777-8888-4999-0000-111122223333', 'CTRL001', 'a1b2c3d4-e5f6-4789-a012-3456789abcde', '89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7'),
('88888888-9999-4000-1111-222233334444', 'CTRL002', 'a1b2c3d4-e5f6-4789-a012-3456789abcde', '416f4967-89b1-49f8-a8d3-134c6e63cf5b'),
('99999999-0000-4111-2222-333344445555', 'CTRL005', 'b2c3d4e5-f6a7-4890-b123-456789abcdef', '04d1f1c0-3022-4e53-9f34-d0136c89c9ed'),
('00000000-1111-4222-3333-444455556666', 'CTRL003', 'c3d4e5f6-a7b8-4901-c234-56789abcdef0', '55555555-6666-4777-8888-999000111222'),
('11111111-2222-4333-4444-555566667777', 'CTRL004', 'c3d4e5f6-a7b8-4901-c234-56789abcdef0', '66666666-7777-4888-9999-000111222333');

-- Tabela assist
INSERT INTO assist (id, caregiver_id, resident_id, called_at, assignment_at, end_at, detail, severity_level) VALUES
('12345678-1111-4222-3333-444455556666', '11111111-2222-4333-a444-555666777888', '89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7', '2025-06-01 09:00:00+00', '2025-06-01 09:05:00+00', '2025-06-01 09:30:00+00', 'Assistência com medicação matinal', 'warning'),
('23456789-2222-4333-4444-555566667777', '22222222-3333-4444-b555-666777888999', '416f4967-89b1-49f8-a8d3-134c6e63cf5b', '2025-06-01 14:00:00+00', '2025-06-01 14:02:00+00', '2025-06-01 14:45:00+00', 'Queda no banheiro - assistência médica', 'emergency'),
('34567890-3333-4444-5555-666677778888', '33333333-4444-5555-c666-777888999000', '04d1f1c0-3022-4e53-9f34-d0136c89c9ed', '2025-06-01 20:00:00+00', '2025-06-01 20:03:00+00', '2025-06-01 20:15:00+00', 'Auxílio para ir ao banheiro', 'warning'),
('45678901-4444-5555-6666-777788889999', '11111111-2222-4333-a444-555666777888', '55555555-6666-4777-8888-999000111222', '2025-06-02 08:30:00+00', '2025-06-02 08:32:00+00', null, 'Mal estar súbito - em atendimento', 'emergency'),
('56789012-5555-6666-7777-888899990000', null, '66666666-7777-4888-9999-000111222333', '2025-06-02 16:45:00+00', null, null, 'Chamado de emergência - aguardando cuidador', 'emergency'),
('67890123-6666-7777-8888-999900001111', null, '99998888-7777-4888-9999-000111222333', '2025-06-02 10:00:00+00', '2025-06-02 10:05:00+00', null, null, 'warning'),
('78901234-7777-8888-9999-000011112222', '11111111-2222-4333-a444-555666777888', '99999999-7777-4888-9999-000111222333', '2025-06-02 11:00:00+00', '2025-06-02 11:10:00+00', '2025-06-01 20:15:00+00', 'Auxílio para ir ao banheiro', 'warning');