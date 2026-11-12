import { useEffect, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { setUnauthorizedHandler } from '../shared/api/httpClient';
import { sessionStore, useSesion } from '../shared/auth/sessionStore';
import type { Sesion } from '../shared/auth/tokenStorage';
import { LoginPage } from '../features/auth/pages/LoginPage';
import { EstudiantesPage } from '../features/estudiantes/pages/EstudiantesPage';

export function App() {
  const queryClient = useQueryClient();
  // Estado de sesión: vive en sessionStore (fuente de verdad: sessionStorage), no en useState ni en la caché.
  const sesion = useSesion();
  // Estado local de interfaz: aviso que se muestra en el login.
  const [aviso, setAviso] = useState<string | null>(null);

  // 401 (token expirado o inválido): se cierra la sesión, se limpia la caché y se vuelve al login.
  // 403 NO pasa por aquí: el usuario sigue autenticado y cada vista muestra el mensaje de permisos.
  useEffect(() => {
    setUnauthorizedHandler(() => {
      sessionStore.cerrar();
      queryClient.clear();
      setAviso('Tu sesión expiró o el token no es válido. Inicia sesión nuevamente.');
    });
    return () => setUnauthorizedHandler(null);
  }, [queryClient]);

  function iniciarSesion(nueva: Sesion) {
    setAviso(null);
    sessionStore.iniciar(nueva);
  }

  function cerrarSesion() {
    sessionStore.cerrar();
    // Al cambiar de usuario no deben quedar en caché datos consultados con el token anterior.
    queryClient.clear();
  }

  if (!sesion) return <LoginPage aviso={aviso} onLogin={iniciarSesion} />;

  return (
    <>
      <header className="barra">
        <strong>Gestión Académica</strong>
        <div className="usuario">
          <span>
            {sesion.username} · <span className="rol">{sesion.rol}</span>
          </span>
          <button className="secundario" onClick={cerrarSesion}>
            Cerrar sesión
          </button>
        </div>
      </header>
      <main className="contenedor">
        <EstudiantesPage />
      </main>
    </>
  );
}
