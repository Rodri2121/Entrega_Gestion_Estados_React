import { httpClient } from '../../../shared/api/httpClient';
import type { Carrera } from '../types';

export function listarCarreras(signal?: AbortSignal): Promise<Carrera[]> {
  return httpClient.get<Carrera[]>('/carreras', signal);
}
