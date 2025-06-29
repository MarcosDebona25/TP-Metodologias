-- Insertar usuario administrativo
INSERT INTO usuario (id, numero_documento, nombre, apellido, email)
VALUES (11999888, '11999888', 'Admin', 'User', 'admin@example.com');

-- Insertar titulares iniciales
INSERT INTO titular (numero_documento, nombre, apellido, fecha_nacimiento, domicilio, grupo_factor, donante_organos)
VALUES
    ('12345678', 'Juan', 'Pérez', '1990-05-15', 'Calle Falsa 123', 'A+', true),
    ('98765321', 'Elber', 'Galarza', '1999-09-25', 'Calle Posta 555', 'O-', false),
    ('17000000', 'Ana', 'Gómez', '2008-06-28', 'Av. Siempreviva 742', 'AB+', true),
    ('20000000', 'Roberto', 'Flores', '2005-06-28', 'Calle inventada 101', 'B-', false),
    ('21000000', 'Carla', 'Diaz', '2004-06-28', 'Avenida Siempre Viva 1', 'O+', true),
    ('66000000', 'Pedro', 'García', '1959-06-28', 'Calle Larga 500', 'A-', true),
    ('16000000', 'Sofía', 'Rodríguez', '2009-06-28', 'Pasaje Corto 10', 'AB-', false);

-- Insertar licencias activas
-- Licencia de Roberto Flores (con licencia B reciente)
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Nada que decir', 'B', '2025-06-28', '2030-06-28', 11999888);
INSERT INTO licencia_activa (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Nada que decir' AND t.numero_documento = '20000000';

-- Licencia de Carla Diaz (con licencia B antigua)
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Apretaba mucho el embrague', 'B', '2023-06-28', '2028-06-28', 11999888);
INSERT INTO licencia_activa (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Apretaba mucho el embrague' AND t.numero_documento = '21000000';

-- Licencia de Pedro García (con licencia B antigua)
INSERT INTO licencia (observaciones, clases, fecha_emision, fecha_vencimiento, usuario_id)
VALUES ('Al jubilado no le gusta la musica, cuidado!', 'B', '2023-06-28', '2028-06-28', 11999888);
INSERT INTO licencia_activa (numero, titular_id)
SELECT l.numero, t.id
FROM licencia l, titular t
WHERE l.observaciones = 'Al jubilado no le gusta la musica, cuidado!' AND t.numero_documento = '66000000';

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

