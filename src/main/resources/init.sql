CREATE DATABASE IF NOT EXISTS proyectoInter;
USE proyectoInter;

-- USUARIOS
CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    rol ENUM('ADMIN', 'TUTOR_CENTRO', 'TUTOR_EMPRESA', 'ALUMNO')
);

-- ALUMNOS
CREATE TABLE IF NOT EXISTS alumno (
    id INT PRIMARY KEY,
    expediente VARCHAR(50),
    curso VARCHAR(50),
    FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- EMPRESAS
CREATE TABLE IF NOT EXISTS empresa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    cif VARCHAR(20) UNIQUE,
    direccion VARCHAR(200),
    contacto VARCHAR(100),
    email VARCHAR(100)
);

-- TUTORES
CREATE TABLE IF NOT EXISTS tutor (
    id INT PRIMARY KEY,
    tipo ENUM('EMPRESA', 'CENTRO'),
    empresa_id INT,
    FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE SET NULL
);

-- PRACTICAS
CREATE TABLE IF NOT EXISTS practica (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alumno_id INT,
    empresa_id INT,
    tutor_empresa_id INT,
    tutor_centro_id INT,
    fecha_inicio DATE,
    fecha_fin DATE,
    estado ENUM('PENDIENTE', 'ACTIVA', 'FINALIZADA'),
    FOREIGN KEY (alumno_id) REFERENCES alumno(id),
    FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    FOREIGN KEY (tutor_empresa_id) REFERENCES tutor(id),
    FOREIGN KEY (tutor_centro_id) REFERENCES tutor(id)
);

-- SEGUIMIENTOS
CREATE TABLE IF NOT EXISTS seguimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    practica_id INT,
    fecha DATE,
    horas FLOAT,
    descripcion TEXT,
    validado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (practica_id) REFERENCES practica(id)
);

-- EVALUACIONES
CREATE TABLE IF NOT EXISTS evaluacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    practica_id INT,
    tutor_id INT,
    nota FLOAT,
    comentarios TEXT,
    FOREIGN KEY (practica_id) REFERENCES practica(id),
    FOREIGN KEY (tutor_id) REFERENCES tutor(id)
);

-- DOCUMENTOS
CREATE TABLE IF NOT EXISTS documento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    practica_id INT,
    tipo VARCHAR(50),
    ruta VARCHAR(255),
    fecha_subida DATE,
    subido_por INT,
    FOREIGN KEY (practica_id) REFERENCES practica(id),
    FOREIGN KEY (subido_por) REFERENCES usuario(id)
);

-- DATOS DE PRUEBA
INSERT INTO usuario (nombre, email, password, rol) 
SELECT 'Admin', 'admin@correo.com', '1234', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'admin@correo.com');

INSERT INTO usuario (nombre, email, password, rol) 
SELECT 'Tutor Centro', 'tutorcentro@correo.com', '1234', 'TUTOR_CENTRO'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'tutorcentro@correo.com');

INSERT INTO usuario (nombre, email, password, rol) 
SELECT 'Tutor Empresa', 'tutorempresa@correo.com', '1234', 'TUTOR_EMPRESA'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'tutorempresa@correo.com');

INSERT INTO usuario (nombre, email, password, rol) 
SELECT 'Alumno Test', 'alumno@correo.com', '1234', 'ALUMNO'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'alumno@correo.com');

-- EMPRESA DE PRUEBA
INSERT INTO empresa (nombre, cif, direccion, contacto, email)
SELECT 'Empresa Test', 'B12345678', 'Calle Mayor 1', 'Juan García', 'empresa@test.com'
WHERE NOT EXISTS (SELECT 1 FROM empresa WHERE cif = 'B12345678');

-- ALUMNO DE PRUEBA (vinculado al usuario con email alumno@correo.com)
INSERT INTO alumno (id, expediente, curso)
SELECT u.id, 'EXP001', '2DAM'
FROM usuario u WHERE u.email = 'alumno@correo.com'
AND NOT EXISTS (SELECT 1 FROM alumno WHERE expediente = 'EXP001');

-- TUTOR EMPRESA DE PRUEBA
INSERT INTO tutor (id, tipo, empresa_id)
SELECT u.id, 'EMPRESA', e.id
FROM usuario u, empresa e
WHERE u.email = 'tutorempresa@correo.com'
AND e.cif = 'B12345678'
AND NOT EXISTS (SELECT 1 FROM tutor WHERE id = (SELECT id FROM usuario WHERE email = 'tutorempresa@correo.com'));

-- TUTOR CENTRO DE PRUEBA
INSERT INTO tutor (id, tipo, empresa_id)
SELECT u.id, 'CENTRO', NULL
FROM usuario u
WHERE u.email = 'tutorcentro@correo.com'
AND NOT EXISTS (SELECT 1 FROM tutor WHERE id = (SELECT id FROM usuario WHERE email = 'tutorcentro@correo.com'));

-- PRACTICA DE PRUEBA
INSERT INTO practica (alumno_id, empresa_id, tutor_empresa_id, tutor_centro_id, fecha_inicio, fecha_fin, estado)
SELECT a.id, e.id, te.id, tc.id, '2026-01-01', '2026-03-31', 'ACTIVA'
FROM alumno a, empresa e, tutor te, tutor tc
WHERE a.expediente = 'EXP001'
AND e.cif = 'B12345678'
AND te.tipo = 'EMPRESA'
AND tc.tipo = 'CENTRO'
AND NOT EXISTS (SELECT 1 FROM practica WHERE alumno_id = a.id);