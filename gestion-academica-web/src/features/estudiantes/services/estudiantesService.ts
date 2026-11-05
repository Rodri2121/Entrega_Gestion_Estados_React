// Acceso a datos del recurso Estudiantes, separado de los componentes.
import { httpClient } from '../../../shared/api/httpClient';
import type { Estudiante } from '../types';

export function listarEstudiantes(signal?: AbortSignal): Promise<Estudiante[]> {
  return httpClient.get<Estudiante[]>('/estudiantes', signal);
}

export function obtenerEstudiante(id: number, signal?: AbortSignal): Promise<Estudiante> {
  return httpClient.get<Estudiante>(`/estudiantes/${id}`, signal);
}
