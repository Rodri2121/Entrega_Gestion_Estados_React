// Lectura centralizada de variables de entorno de Vite.
export const env = {
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL ?? '/api/v1',
} as const;
