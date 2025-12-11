CREATE DATABASE IF NOT EXISTS proyectoInter;
USE proyectoInter;

CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    email VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    rol ENUM('ADMIN', 'TUTOR_CENTRO', 'TUTOR_EMPRESA', 'ALUMNO')
);

INSERT INTO usuario (nombre, email, password, rol)
VALUES ('Admin', 'admin@correo.com', '1234', 'ADMIN');
