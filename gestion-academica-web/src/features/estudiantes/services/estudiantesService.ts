// Acceso a datos del recurso Estudiantes, separado de los componentes.
import { httpClient } from '../../../shared/api/httpClient';
import type { Estudiante, EstudianteRequest } from '../types';

export function listarEstudiantes(signal?: AbortSignal): Promise<Estudiante[]> {
  return httpClient.get<Estudiante[]>('/estudiantes', signal);
}

export function obtenerEstudiante(id: number, signal?: AbortSignal): Promise<Estudiante> {
  return httpClient.get<Estudiante>(`/estudiantes/${id}`, signal);
}

/** POST /api/v1/estudiantes → 201 + EstudianteResponse (requiere ADMIN). */
export function crearEstudiante(datos: EstudianteRequest): Promise<Estudiante> {
  return httpClient.post<Estudiante>('/estudiantes', datos);
}

/** PUT /api/v1/estudiantes/{id} → 200 + EstudianteResponse (requiere ADMIN). */
export function actualizarEstudiante(id: number, datos: EstudianteRequest): Promise<Estudiante> {
  return httpClient.put<Estudiante>(`/estudiantes/${id}`, datos);
}
