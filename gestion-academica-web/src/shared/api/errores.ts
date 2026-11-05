import { ApiError } from './httpClient';

/** Solo tiene sentido reintentar fallas de red o errores del servidor; 4xx devolvería lo mismo. */
export function esReintentable(error: unknown): boolean {
  if (!(error instanceof ApiError)) return true;
  return error.kind === 'network' || error.status === undefined || error.status >= 500;
}

export function esProhibido(error: unknown): boolean {
  return error instanceof ApiError && error.status === 403;
}
