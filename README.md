# Sesión 12 · Consumo de una API REST de Spring Boot desde React y TypeScript

**Asignatura:** SIS0421 Servicios Web · Unidad III: Consumo de API
**Proyecto:** Gestión Académica (API Spring Boot + frontend React)
**Fecha:** 04/11/2026

El análisis completo (contrato de la API, decisiones de implementación, pruebas y reflexión) está en
[`DOCUMENTACION.md`](DOCUMENTACION.md) y en su versión imprimible `DOCUMENTACION.pdf`.

## Contenido de la entrega

| Carpeta | Contenido |
|---|---|
| `gestion-academica/` | API Spring Boot 4 + PostgreSQL + JWT (puerto 9090) |
| `gestion-academica-web/` | Frontend React 18 + TypeScript + Vite + TanStack Query (puerto 5173) |
| `postman/` | Colección para contrastar el frontend con Postman |
| `scripts/` | Base de datos, arranque de la API y carga de datos de prueba |

## Requisitos

- Java 21 y Maven
- Node.js 18 o superior
- PostgreSQL (se probó con PostgreSQL 18)

## Ejecución

Todos los comandos se ejecutan desde la carpeta de la entrega.

### 1. Base de datos

La API espera `jdbc:postgresql://localhost:5432/universidad` con el usuario `admin` / `admin1234`.

- **Opción A – PostgreSQL instalado (puerto 5432):** ejecutar una vez con el superusuario:
  ```
  psql -U postgres -h localhost -f scripts\crear-base-datos.sql
  ```
- **Opción B – servidor propio del proyecto (puerto 5433):** no necesita la contraseña de `postgres`:
  ```
  powershell -ExecutionPolicy Bypass -File scripts\base-datos-local.ps1 -Accion iniciar
  ```

### 2. API Spring Boot

```
powershell -ExecutionPolicy Bypass -File scripts\iniciar-api.ps1              # opción A
powershell -ExecutionPolicy Bypass -File scripts\iniciar-api.ps1 -PuertoBD 5433 # opción B
```

El script define `JWT_SECRET` y `ADMIN_PASSWORD` (variables obligatorias de la API). Al primer arranque
se crea el usuario `admin` / `Admin12345`.

### 3. Datos de prueba (solo la primera vez)

```
powershell -ExecutionPolicy Bypass -File scripts\cargar-datos-prueba.ps1
```

Crea 2 carreras, 3 estudiantes y los usuarios `docente1` / `Docente123` y `estudiante1` / `Estudiante123`.

### 4. Frontend React

```
cd gestion-academica-web
pnpm install      # o: npm install
pnpm dev          # o: npm run dev
```

Abrir http://localhost:5173 e iniciar sesión con `admin` / `Admin12345`.

> Las credenciales anteriores son solo para el entorno local de desarrollo.

## Guion de la demostración

1. **Datos:** iniciar sesión como `admin` o `docente1` → tabla con los estudiantes reales.
2. **Detalle (Actividad 3, opción A):** clic en una fila → panel con `GET /estudiantes/{id}`.
3. **Carga:** DevTools → Network → *Slow 3G* → **Actualizar**.
4. **Sin resultados:** con la base vacía (antes de cargar los datos de prueba) se muestra el mensaje de lista vacía.
   Si ya hay datos, se puede comprobar el mismo estado con una base nueva o borrando los estudiantes desde Postman.
5. **Falla de conexión:** detener la API → **Actualizar** → mensaje de error y botón *Reintentar*.
6. **403:** iniciar sesión como `estudiante1` → mensaje de permisos insuficientes.
7. **401:** reiniciar la API (genera un `JWT_SECRET` nuevo) → **Actualizar** → la sesión se cierra y vuelve al login.
8. **Contraste con Postman:** importar `postman/GestionAcademica-S12.postman_collection.json` y comparar con la pestaña Network.
