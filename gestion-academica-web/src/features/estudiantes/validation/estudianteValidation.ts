// Validación del formulario de estudiantes.
// - Cliente: replica las validaciones técnicas del DTO (EstudianteRequest) para dar respuesta inmediata.
// - Servidor: los errores que devuelve la API se traducen al mismo formato (campo → mensaje).
// La validación del cliente es una comodidad; la que manda es la del backend.
import { ApiError } from '../../../shared/api/httpClient';
import type { CampoEstudiante, EstudianteRequest } from '../types';

/** Valores tal como están escritos en el formulario (todo es texto en un <input>). */
export interface ValoresFormulario {
  nombre: string;
  correo: string;
  edad: string;
  carrera: string;
}

export type ErroresCampo = Partial<Record<CampoEstudiante, string>>;

export const VALORES_VACIOS: ValoresFormulario = { nombre: '', correo: '', edad: '', carrera: '' };

// Mismo criterio práctico que @Email: algo@dominio.ext, sin espacios.
const CORREO_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export function validarEstudiante(v: ValoresFormulario): ErroresCampo {
  const errores: ErroresCampo = {};

  if (!v.nombre.trim()) errores.nombre = 'El nombre es obligatorio.';
  else if (v.nombre.trim().length < 3) errores.nombre = 'El nombre debe tener al menos 3 caracteres.';

  if (!v.correo.trim()) errores.correo = 'El correo es obligatorio.';
  else if (!CORREO_REGEX.test(v.correo.trim())) errores.correo = 'El formato del correo es inválido.';

  if (!v.edad.trim()) errores.edad = 'La edad es obligatoria.';
  else {
    const edad = Number(v.edad);
    if (!Number.isInteger(edad)) errores.edad = 'La edad debe ser un número entero.';
    else if (edad < 1) errores.edad = 'La edad debe ser positiva.';
    else if (edad > 120) errores.edad = 'La edad no es válida.';
    // La regla "mayor de edad" es del DOMINIO (Estudiante.validateCanRegister) y la responde la API.
  }

  if (!v.carrera) errores.carrera = 'Selecciona una carrera.';

  return errores;
}

export function aRequest(v: ValoresFormulario): EstudianteRequest {
  return {
    nombre: v.nombre.trim(),
    correo: v.correo.trim(),
    edad: Number(v.edad),
    carrera: Number(v.carrera),
  };
}

export interface ErrorServidor {
  campos: ErroresCampo;
  general: string | null;
}

/**
 * Traduce un error de la API a errores por campo + mensaje general:
 * - 400 "Validation Error": body.errors = { campo: mensaje } (@Valid del DTO).
 * - 400 regla de negocio (menor de edad) → campo edad.
 * - 409 correo duplicado → campo correo.
 * - 404 carrera inexistente → campo carrera.
 * - 403, red, 5xx… → solo mensaje general.
 */
export function erroresDesdeApi(error: unknown): ErrorServidor {
  if (!(error instanceof ApiError)) return { campos: {}, general: 'Ocurrió un error inesperado.' };

  const campos: ErroresCampo = {};
  const errs = error.body?.errors;
  if (errs) {
    for (const [campo, mensaje] of Object.entries(errs)) {
      if (campo in VALORES_VACIOS) campos[campo as CampoEstudiante] = mensaje;
    }
    return { campos, general: 'La API rechazó los datos. Revisa los campos marcados.' };
  }

  const msg = error.message;
  if (error.status === 409) campos.correo = msg;
  else if (error.status === 404 && /carrera/i.test(msg)) campos.carrera = msg;
  else if (error.status === 400 && /edad/i.test(msg)) campos.edad = msg;

  if (error.status === 403) {
    return { campos, general: `${msg} Solo el rol ADMIN puede registrar o modificar estudiantes.` };
  }
  return { campos, general: Object.keys(campos).length > 0 ? 'La API rechazó la operación.' : msg };
}
