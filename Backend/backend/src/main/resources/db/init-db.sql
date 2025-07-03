-- Insertar usuario administrativo con rol y contraseña hasheada
-- Contraseña: "admin123" hasheada con BCrypt: $2a$10$9T5R0sMoQ5iVIRpokZnuIeb7XoV6jewwTk/i8p4xwAxmKkY7p6g4i
INSERT INTO usuario (id, numero_documento, nombre, apellido, email, rol, contrasena)
VALUES (11999888, '11999888', 'Admin', 'Regular', 'admin@example.com', 'ADMIN', '$2a$10$9T5R0sMoQ5iVIRpokZnuIeb7XoV6jewwTk/i8p4xwAxmKkY7p6g4i');

-- OPCIONAL: Agregar un usuario adicional con rol USER para pruebas
-- Contraseña: "user123" hasheada con BCrypt: $2a$10$J0mLVRrHCbD6Gxr9E8cr/.ZF72EfNmxqYWdV3kEXrRwQsOJ7mUg/e
INSERT INTO usuario (id, numero_documento, nombre, apellido, email, rol, contrasena)
VALUES (22000111, '22000111', 'Usuario', 'Regular', 'user@example.com', 'USER', '$2a$10$J0mLVRrHCbD6Gxr9E8cr/.ZF72EfNmxqYWdV3kEXrRwQsOJ7mUg/e');

-- Insertar titulares iniciales
INSERT INTO titular (numero_documento, nombre, apellido, fecha_nacimiento, domicilio, grupo_factor, donante_organos)
VALUES
    ('12345678', 'Juan', 'Pérez', '1990-05-15', 'Calle Falsa 123', 'A+', true),
    ('98765321', 'Elber', 'Galarza', '1999-09-25', 'Calle Posta 555', 'O-', false),
    ('17000000', 'Ana', 'Gómez', '2008-06-28', 'Av. Siempreviva 742', 'AB+', true),
    ('20000000', 'Roberto', 'Flores', '2005-06-28', 'Calle inventada 101', 'B-', false),
    ('21000000', 'Carla', 'Diaz', '2004-06-28', 'Avenida Siempre Viva 1', 'O+', true),
    ('66000000', 'Pedro', 'García', '1959-06-28', 'Calle Larga 500', 'A-', true),
    ('16000000', 'Sofía', 'Rodríguez', '2009-06-28', 'Pasaje Corto 10', 'AB-', false),
    ('30000000', 'Laura', 'Ramírez', '1990-01-01', 'Calle Ficticia 100', 'A-', true), -- Para probar nueva emisión de licencia B
    ('35000000', 'Carlos', 'Vega', '1980-02-02', 'Av. Imaginaria 200', 'O+', false), -- Para probar emisión de licencia D con B antigua
    ('40000000', 'Elena', 'Montes', '2000-03-03', 'Paseo de los Sueños 300', 'AB+', true), -- Para probar emisión de licencia C con B reciente
    ('45000000', 'Fernando', 'Ruiz', '1970-04-04', 'Boulevard del Sol 400', 'B+', false), -- Para probar renovación de licencia B a D
    ('50000000', 'Gabriela', 'Núñez', '2009-07-01', 'Plaza Central 50', 'O-', false), -- Para probar emisión de licencia con edad insuficiente
    ('55000000', 'Hugo', 'Paz', '1955-05-05', 'Camino Real 500', 'A+', true); -- Para probar primera licencia profesional con edad avanzada

-- Insertar licencias activas
-- Licencia de Roberto Flores (con licencia B reciente, emitida hoy)
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Nada que decir', 'B', '2025-06-28', '2030-06-28', 11999888);
INSERT INTO licencia_activa (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Nada que decir' AND t.numero_documento = '20000000';

-- Licencia de Carla Diaz (con licencia B antigua, emitida hace 2 años)
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Apretaba mucho el embrague', 'B', '2023-06-28', '2028-06-28', 11999888);
INSERT INTO licencia_activa (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Apretaba mucho el embrague' AND t.numero_documento = '21000000';

-- Licencia de Pedro García (con licencia B antigua, emitida hace 2 años)
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Al jubilado no le gusta la musica, cuidado!', 'B', '2023-06-28', '2028-06-28', 11999888);
INSERT INTO licencia_activa (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Al jubilado no le gusta la musica, cuidado!' AND t.numero_documento = '66000000';

-- Licencia B antigua para Carlos Vega (para probar licencia D)
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Licencia B antigua para Carlos', 'B', '2023-01-01', '2028-01-01', 11999888);
INSERT INTO licencia_activa (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Licencia B antigua para Carlos' AND t.numero_documento = '35000000';

-- Licencia B para Fernando Ruiz (para probar renovación a D)
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Licencia B para Fernando', 'B', '2022-06-28', '2027-06-28', 11999888);
INSERT INTO licencia_activa (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Licencia B para Fernando' AND t.numero_documento = '45000000';


-- Ejemplos de licencias expiradas
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Licencia B expirada', 'B', '2010-01-01', '2015-01-01', 11999888);
INSERT INTO licencia_expirada (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Licencia B expirada' AND t.numero_documento = '12345678';

INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Licencia D expirada', 'D', '2016-03-10', '2021-03-10', 11999888);
INSERT INTO licencia_expirada (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Licencia D expirada' AND t.numero_documento = '12345678';

-- Agregar titular con licencia vencida para probar renovaciones
INSERT INTO titular (numero_documento, nombre, apellido, fecha_nacimiento, domicilio, grupo_factor, donante_organos)
VALUES ('60000000', 'María', 'Vencida', '1985-12-01', 'Calle Vieja 123', 'O+', true);

-- Crear licencia B que venció ayer
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Licencia B vencida para renovar', 'B', '2020-06-28', '2025-06-28', 11999888);

-- Asignar como licencia activa (aunque esté vencida, sigue siendo "activa" hasta que se renueve)
INSERT INTO licencia_activa (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Licencia B vencida para renovar' AND t.numero_documento = '60000000';