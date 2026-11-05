-- Crea el usuario y la base de datos que espera application.properties.
-- Ejecutar UNA sola vez con un superusuario de PostgreSQL, por ejemplo:
--   psql -U postgres -h localhost -f crear-base-datos.sql
-- Hibernate (ddl-auto=update) crea las tablas al iniciar la API.

CREATE ROLE admin WITH LOGIN PASSWORD 'admin1234';
CREATE DATABASE universidad OWNER admin;
