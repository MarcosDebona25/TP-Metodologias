# # DROP DATABASE IF EXISTS licenciasdb;
# # DROP USER IF EXISTS `licenciasadmin`@`%`;
# # CREATE DATABASE IF NOT EXISTS licenciasdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
# # CREATE USER IF NOT EXISTS `licenciasadmin`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
# # GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
# # CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `licenciasdb`.* TO `licenciasadmin`@`%`;
# #

-- Insertar Usuarios Administrativos
INSERT INTO usuario (nombre, apellido, email, numero_documento) VALUES
                                                                    ('Juan', 'Perez', 'juan.perez@example.com', '12345678'),
                                                                    ('Maria', 'Gomez', 'maria.gomez@example.com', '87654321'),
                                                                    ('Carlos', 'Rodriguez', 'carlos.rodriguez@example.com', '11223344'),
                                                                    ('Ana', 'Lopez', 'ana.lopez@example.com', '44332211'),
                                                                    ('Pedro', 'Martinez', 'pedro.martinez@example.com', '55667788'),
                                                                    ('Laura', 'Diaz', 'laura.diaz@example.com', '88776655'),
                                                                    ('Sofia', 'Sanchez', 'sofia.sanchez@example.com', '99001122'),
                                                                    ('Diego', 'Ramirez', 'diego.ramirez@example.com', '22110099'),
                                                                    ('Elena', 'Torres', 'elena.torres@example.com', '33445566'),
                                                                    ('Pablo', 'Acosta', 'pablo.acosta@example.com', '66554433');

-- Insertar Titulares
INSERT INTO titular (nombre, apellido, numero_documento, fecha_nacimiento, domicilio, grupo_factor, donante_organos) VALUES
                                                                                                                         ('Martin', 'Fernandez', '20123456', '1985-03-15', 'Calle Falsa 123', 'A+', true),
                                                                                                                         ('Lucia', 'Gimenez', '21234567', '1990-07-22', 'Av. Siempreviva 742', 'O-', false),
                                                                                                                         ('Andres', 'Ruiz', '22345678', '1978-01-05', 'Bv. Oroño 456', 'B+', true),
                                                                                                                         ('Valeria', 'Silva', '23456789', '1995-11-30', 'Salta 890', 'AB-', false),
                                                                                                                         ('Gonzalo', 'Castro', '24567890', '1982-09-10', 'San Juan 1011', 'O+', true),
                                                                                                                         ('Florencia', 'Blanco', '25678901', '1998-04-18', 'Entre Rios 1213', 'A-', false),
                                                                                                                         ('Nicolas', 'Herrera', '26789012', '1975-12-25', 'Corrientes 1415', 'B-', true),
                                                                                                                         ('Camila', 'Morales', '27890123', '2000-06-03', 'Rivadavia 1617', 'AB+', false),
                                                                                                                         ('Santiago', 'Vazquez', '28901234', '1987-02-14', 'Belgrano 1819', 'O-', true),
                                                                                                                         ('Agustina', 'Romero', '29012345', '1993-08-08', 'Mitre 2021', 'A+', false),
                                                                                                                         ('Federico', 'Sosa', '30123456', '1980-10-01', 'Libertad 2223', 'O+', true),
                                                                                                                         ('Julieta', 'Cordoba', '31234567', '1997-05-19', 'Santa Fe 2425', 'B+', false),
                                                                                                                         ('Ezequiel', 'Mendez', '32345678', '1970-03-20', 'Córdoba 2627', 'A-', true),
                                                                                                                         ('Carolina', 'Paz', '33456789', '1992-09-28', 'La Rioja 2829', 'AB-', false),
                                                                                                                         ('Guillermo', 'Vega', '34567890', '1988-01-11', 'Catamarca 3031', 'O+', true),
                                                                                                                         ('Mariana', 'Fuentes', '35678901', '1999-07-07', 'Chaco 3233', 'A+', false),
                                                                                                                         ('Ricardo', 'Ortiz', '36789012', '1973-04-04', 'Formosa 3435', 'B-', true),
                                                                                                                         ('Lorena', 'Rojas', '37890123', '1994-11-21', 'Misiones 3637', 'AB+', false),
                                                                                                                         ('Lucas', 'Prieto', '38901234', '1983-06-16', 'Jujuy 3839', 'O-', true),
                                                                                                                         ('Daniela', 'Serrano', '39012345', '1991-02-20', 'Salta 4041', 'A-', false),
                                                                                                                         ('Gabriel', 'Nuñez', '40123456', '1977-08-12', 'Tucuman 4243', 'B+', true),
                                                                                                                         ('Rocio', 'Maidana', '41234567', '1996-03-01', 'San Luis 4445', 'O+', false),
                                                                                                                         ('Jorge', 'Figueroa', '42345678', '1981-10-09', 'San Juan 4647', 'A+', true),
                                                                                                                         ('Monica', 'Delgado', '43456789', '1979-05-24', 'La Pampa 4849', 'B-', false),
                                                                                                                         ('Hugo', 'Aguirre', '44567890', '1984-12-06', 'Neuquen 5051', 'O-', true);

-- Insertar Licencias (activas y expiradas)
-- Un titular con licencia activa y varias expiradas
INSERT INTO licencia (fecha_emision, fecha_vencimiento, usuario_id, clases, observaciones) VALUES
                                                                                               ('2024-06-27', '2029-06-27', (SELECT id FROM usuario WHERE numero_documento = '12345678'), 'A, B1', 'Licencia activa de Martin Fernandez'),
                                                                                               ('2019-06-27', '2024-06-26', (SELECT id FROM usuario WHERE numero_documento = '12345678'), 'A', 'Licencia expirada anterior de Martin Fernandez'),
                                                                                               ('2014-06-27', '2019-06-26', (SELECT id FROM usuario WHERE numero_documento = '12345678'), 'B1', 'Licencia expirada más antigua de Martin Fernandez');

INSERT INTO licencia_activa (numero, titular_id) VALUES
    ((SELECT numero FROM licencia WHERE observaciones = 'Licencia activa de Martin Fernandez'), (SELECT id FROM titular WHERE numero_documento = '20123456'));

INSERT INTO licencia_expirada (numero, titular_id) VALUES
                                                       ((SELECT numero FROM licencia WHERE observaciones = 'Licencia expirada anterior de Martin Fernandez'), (SELECT id FROM titular WHERE numero_documento = '20123456')),
                                                       ((SELECT numero FROM licencia WHERE observaciones = 'Licencia expirada más antigua de Martin Fernandez'), (SELECT id FROM titular WHERE numero_documento = '20123456'));

-- Otro titular con licencia activa
INSERT INTO licencia (fecha_emision, fecha_vencimiento, usuario_id, clases, observaciones) VALUES
    ('2023-01-10', '2028-01-10', (SELECT id FROM usuario WHERE numero_documento = '87654321'), 'B1', 'Licencia activa de Lucia Gimenez');

INSERT INTO licencia_activa (numero, titular_id) VALUES
    ((SELECT numero FROM licencia WHERE observaciones = 'Licencia activa de Lucia Gimenez'), (SELECT id FROM titular WHERE numero_documento = '21234567'));

-- Otro titular con solo licencias expiradas
INSERT INTO licencia (fecha_emision, fecha_vencimiento, usuario_id, clases, observaciones) VALUES
                                                                                               ('2015-05-01', '2020-05-01', (SELECT id FROM usuario WHERE numero_documento = '11223344'), 'C', 'Licencia expirada de Andres Ruiz (primera)'),
                                                                                               ('2020-05-02', '2025-05-01', (SELECT id FROM usuario WHERE numero_documento = '11223344'), 'C, D', 'Licencia expirada de Andres Ruiz (segunda)');

INSERT INTO licencia_expirada (numero, titular_id) VALUES
                                                       ((SELECT numero FROM licencia WHERE observaciones = 'Licencia expirada de Andres Ruiz (primera)'), (SELECT id FROM titular WHERE numero_documento = '22345678')),
                                                       ((SELECT numero FROM licencia WHERE observaciones = 'Licencia expirada de Andres Ruiz (segunda)'), (SELECT id FROM titular WHERE numero_documento = '22345678'));

-- Más licencias activas para diversos titulares
INSERT INTO licencia (fecha_emision, fecha_vencimiento, usuario_id, clases, observaciones) VALUES
                                                                                               ('2025-01-01', '2030-01-01', (SELECT id FROM usuario WHERE numero_documento = '44332211'), 'B1, B2', 'Licencia activa de Valeria Silva'),
                                                                                               ('2024-03-20', '2029-03-20', (SELECT id FROM usuario WHERE numero_documento = '55667788'), 'A, B1, C', 'Licencia activa de Gonzalo Castro'),
                                                                                               ('2023-09-15', '2028-09-15', (SELECT id FROM usuario WHERE numero_documento = '88776655'), 'A', 'Licencia activa de Florencia Blanco'),
                                                                                               ('2025-02-10', '2030-02-10', (SELECT id FROM usuario WHERE numero_documento = '99001122'), 'B1', 'Licencia activa de Nicolas Herrera'),
                                                                                               ('2024-07-05', '2029-07-05', (SELECT id FROM usuario WHERE numero_documento = '22110099'), 'C, E', 'Licencia activa de Camila Morales'),
                                                                                               ('2023-11-22', '2028-11-22', (SELECT id FROM usuario WHERE numero_documento = '33445566'), 'D', 'Licencia activa de Santiago Vazquez'),
                                                                                               ('2025-04-12', '2030-04-12', (SELECT id FROM usuario WHERE numero_documento = '66554433'), 'A, C', 'Licencia activa de Agustina Romero');

INSERT INTO licencia_activa (numero, titular_id) VALUES
                                                     ((SELECT numero FROM licencia WHERE observaciones = 'Licencia activa de Valeria Silva'), (SELECT id FROM titular WHERE numero_documento = '23456789')),
                                                     ((SELECT numero FROM licencia WHERE observaciones = 'Licencia activa de Gonzalo Castro'), (SELECT id FROM titular WHERE numero_documento = '24567890')),
                                                     ((SELECT numero FROM licencia WHERE observaciones = 'Licencia activa de Florencia Blanco'), (SELECT id FROM titular WHERE numero_documento = '25678901')),
                                                     ((SELECT numero FROM licencia WHERE observaciones = 'Licencia activa de Nicolas Herrera'), (SELECT id FROM titular WHERE numero_documento = '26789012')),
                                                     ((SELECT numero FROM licencia WHERE observaciones = 'Licencia activa de Camila Morales'), (SELECT id FROM titular WHERE numero_documento = '27890123')),
                                                     ((SELECT numero FROM licencia WHERE observaciones = 'Licencia activa de Santiago Vazquez'), (SELECT id FROM titular WHERE numero_documento = '28901234')),
                                                     ((SELECT numero FROM licencia WHERE observaciones = 'Licencia activa de Agustina Romero'), (SELECT id FROM titular WHERE numero_documento = '29012345'));

-- Más licencias expiradas para diversos titulares (con algunos que no tienen activa)
INSERT INTO licencia (fecha_emision, fecha_vencimiento, usuario_id, clases, observaciones) VALUES
                                                                                               ('2018-01-01', '2023-01-01', (SELECT id FROM usuario WHERE numero_documento = '12345678'), 'B1', 'Licencia expirada de Federico Sosa'),
                                                                                               ('2017-06-10', '2022-06-10', (SELECT id FROM usuario WHERE numero_documento = '87654321'), 'A', 'Licencia expirada de Julieta Cordoba'),
                                                                                               ('2016-03-05', '2021-03-05', (SELECT id FROM usuario WHERE numero_documento = '11223344'), 'D', 'Licencia expirada de Ezequiel Mendez'),
                                                                                               ('2015-09-11', '2020-09-11', (SELECT id FROM usuario WHERE numero_documento = '44332211'), 'B2', 'Licencia expirada de Carolina Paz'),
                                                                                               ('2019-12-30', '2024-12-30', (SELECT id FROM usuario WHERE numero_documento = '55667788'), 'A', 'Licencia expirada de Guillermo Vega');

INSERT INTO licencia_expirada (numero, titular_id) VALUES
                                                       ((SELECT numero FROM licencia WHERE observaciones = 'Licencia expirada de Federico Sosa'), (SELECT id FROM titular WHERE numero_documento = '30123456')),
                                                       ((SELECT numero FROM licencia WHERE observaciones = 'Licencia expirada de Julieta Cordoba'), (SELECT id FROM titular WHERE numero_documento = '31234567')),
                                                       ((SELECT numero FROM licencia WHERE observaciones = 'Licencia expirada de Ezequiel Mendez'), (SELECT id FROM titular WHERE numero_documento = '32345678')),
                                                       ((SELECT numero FROM licencia WHERE observaciones = 'Licencia expirada de Carolina Paz'), (SELECT id FROM titular WHERE numero_documento = '33456789')),
                                                       ((SELECT numero FROM licencia WHERE observaciones = 'Licencia expirada de Guillermo Vega'), (SELECT id FROM titular WHERE numero_documento = '34567890'));

-- Más titulares sin licencias actualmente
INSERT INTO titular (nombre, apellido, numero_documento, fecha_nacimiento, domicilio, grupo_factor, donante_organos) VALUES
                                                                                                                         ('Laura', 'Peralta', '45678901', '1990-02-01', 'Av. del Libertador 100', 'O+', false),
                                                                                                                         ('Roberto', 'Caceres', '46789012', '1985-07-17', 'Soler 200', 'A-', true),
                                                                                                                         ('Monica', 'Gallo', '47890123', '1993-04-29', 'Juncal 300', 'B+', false);