// Guarda la sesión en sessionStorage: sobrevive a recargar la página pero se borra al cerrar la pestaña.
// Es un compromiso para el entorno académico; cualquier script de la página podría leerlo (riesgo XSS),
// por eso no se usa localStorage y el token nunca se muestra en pantalla.

const KEY = 'gestion-academica.sesion';

export interface Sesion {
  accessToken: string;
  username: string;
  rol: string;
}

export function leerSesion(): Sesion | null {
  try {
    const raw = sessionStorage.getItem(KEY);
    return raw ? (JSON.parse(raw) as Sesion) : null;
  } catch {
    return null;
  }
}

export function guardarSesion(sesion: Sesion): void {
  try {
    sessionStorage.setItem(KEY, JSON.stringify(sesion));
  } catch {
    // Si el almacenamiento no está disponible, la sesión dura solo mientras la página siga abierta.
  }
}

export function borrarSesion(): void {
  try {
    sessionStorage.removeItem(KEY);
  } catch {
    // sin acción
  }
}
