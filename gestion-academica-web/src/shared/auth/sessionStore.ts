// Estado de SESIÓN: un store externo mínimo sobre sessionStorage.
// - Fuente de verdad: sessionStorage (sobrevive a refrescar la pestaña).
// - No vive en TanStack Query porque no es un dato del servidor que se pueda volver a consultar:
//   lo produce el login una sola vez y lo leen el httpClient (token) y la interfaz (rol).
// - Los componentes lo leen con useSyncExternalStore, así todos se actualizan al iniciar o cerrar sesión.
import { useSyncExternalStore } from 'react';
import type { Rol } from '../../features/auth/types';
import { borrarSesion, guardarSesion, leerSesion, type Sesion } from './tokenStorage';

type Listener = () => void;

let actual: Sesion | null = leerSesion();
const listeners = new Set<Listener>();

function notificar() {
  listeners.forEach((l) => l());
}

export const sessionStore = {
  get: (): Sesion | null => actual,
  subscribe(listener: Listener): () => void {
    listeners.add(listener);
    return () => listeners.delete(listener);
  },
  iniciar(sesion: Sesion): void {
    guardarSesion(sesion);
    actual = sesion;
    notificar();
  },
  cerrar(): void {
    borrarSesion();
    actual = null;
    notificar();
  },
};

export interface UsuarioActual {
  username: string;
  rol: Rol;
  esAdmin: boolean;
  /** Puede crear y modificar estudiantes (POST/PUT requieren ADMIN en el backend). */
  puedeEditarEstudiantes: boolean;
  /** Puede consultar la lista de estudiantes (GET requiere ADMIN o DOCENTE). */
  puedeVerEstudiantes: boolean;
}

/** Sesión cruda (incluye el token): solo para App / httpClient. */
export function useSesion(): Sesion | null {
  return useSyncExternalStore(sessionStore.subscribe, sessionStore.get);
}

/**
 * Usuario actual y permisos DERIVADOS del rol.
 * Ocultar botones en React es solo una ayuda visual: la autorización real la hace Spring Security
 * con @PreAuthorize, y el backend responde 403 aunque alguien fuerce la petición.
 */
export function useCurrentUser(): UsuarioActual | null {
  const sesion = useSesion();
  if (!sesion) return null;
  const rol = sesion.rol as Rol;
  return {
    username: sesion.username,
    rol,
    esAdmin: rol === 'ADMIN',
    puedeEditarEstudiantes: rol === 'ADMIN',
    puedeVerEstudiantes: rol === 'ADMIN' || rol === 'DOCENTE',
  };
}
