// Cliente HTTP compartido.
// Corrección respecto a la base entregada: los tipos y funciones viven a nivel de módulo
// (no anidados dentro de una clase) y `env` se importa explícitamente.
import { env } from '../config/env';
import { leerSesion } from '../auth/tokenStorage';

/** Cuerpo de error que devuelve la API (GlobalExceptionHandler y handlers de seguridad). */
export interface ApiErrorBody {
  timestamp?: string;
  status?: number | string;
  error?: string;
  message?: string;
  errors?: Record<string, string>;
}

export type ApiErrorKind = 'http' | 'network' | 'parse';

export class ApiError extends Error {
  readonly kind: ApiErrorKind;
  readonly status?: number;
  readonly body?: ApiErrorBody;

  constructor(kind: ApiErrorKind, message: string, status?: number, body?: ApiErrorBody) {
    super(message);
    this.name = 'ApiError';
    this.kind = kind;
    this.status = status;
    this.body = body;
  }
}

export interface RequestOptions {
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
  body?: unknown;
  signal?: AbortSignal;
  /** false para endpoints públicos como /auth/login. */
  auth?: boolean;
}

// Se notifica cuando la API responde 401 (token ausente, inválido o expirado).
let onUnauthorized: (() => void) | null = null;

export function setUnauthorizedHandler(handler: (() => void) | null): void {
  onUnauthorized = handler;
}

const MENSAJES_POR_ESTADO: Record<number, string> = {
  400: 'La solicitud contiene datos inválidos.',
  401: 'Tu sesión no es válida o expiró. Inicia sesión nuevamente.',
  403: 'Tu usuario no tiene permisos para realizar esta operación.',
  404: 'El registro solicitado no existe.',
  409: 'La operación entra en conflicto con datos existentes.',
  500: 'Ocurrió un error interno en el servidor.',
};

async function leerCuerpoError(response: Response): Promise<ApiErrorBody | undefined> {
  try {
    return (await response.json()) as ApiErrorBody;
  } catch {
    return undefined;
  }
}

export async function request<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const { method = 'GET', body, signal, auth = true } = options;

  const headers: Record<string, string> = { Accept: 'application/json' };
  if (body !== undefined) headers['Content-Type'] = 'application/json';

  const sesion = leerSesion();
  if (auth && sesion) headers.Authorization = `Bearer ${sesion.accessToken}`;

  let response: Response;
  try {
    response = await fetch(`${env.apiBaseUrl}${path}`, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
      signal,
    });
  } catch (err) {
    if (err instanceof DOMException && err.name === 'AbortError') throw err;
    throw new ApiError('network', 'No se pudo conectar con la API. Verifica que el backend esté en ejecución.');
  }

  // fetch no rechaza la promesa ante 4xx/5xx: se revisa response.ok.
  if (!response.ok) {
    const errorBody = await leerCuerpoError(response);
    // Con el proxy de Vite, si el backend está apagado el proxy responde 500/502/504 sin cuerpo JSON.
    if (!errorBody && response.status >= 500) {
      throw new ApiError('network', 'No se pudo conectar con la API. Verifica que el backend esté en ejecución.', response.status);
    }
    if (response.status === 401 && auth) onUnauthorized?.();
    const mensaje = errorBody?.message || MENSAJES_POR_ESTADO[response.status] || `Error HTTP ${response.status}.`;
    throw new ApiError('http', mensaje, response.status, errorBody);
  }

  // 204 No Content: no hay cuerpo que convertir.
  if (response.status === 204) return undefined as T;

  try {
    return (await response.json()) as T;
  } catch {
    throw new ApiError('parse', 'La respuesta de la API no tiene un formato JSON válido.', response.status);
  }
}

export const httpClient = {
  get: <T>(path: string, signal?: AbortSignal) => request<T>(path, { signal }),
  post: <T>(path: string, body: unknown, options: Omit<RequestOptions, 'method' | 'body'> = {}) =>
    request<T>(path, { ...options, method: 'POST', body }),
  put: <T>(path: string, body: unknown, options: Omit<RequestOptions, 'method' | 'body'> = {}) =>
    request<T>(path, { ...options, method: 'PUT', body }),
};
