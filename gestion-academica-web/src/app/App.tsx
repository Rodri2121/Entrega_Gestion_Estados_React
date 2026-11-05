import { useEffect, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { setUnauthorizedHandler } from '../shared/api/httpClient';
import { borrarSesion, guardarSesion, leerSesion, type Sesion } from '../shared/auth/tokenStorage';
import { LoginPage } from '../features/auth/pages/LoginPage';
import { EstudiantesPage } from '../features/estudiantes/pages/EstudiantesPage';

export function App() {
  const queryClient = useQueryClient();
  const [sesion, setSesion] = useState<Sesion | null>(() => leerSesion());
  const [aviso, setAviso] = useState<string | null>(null);

  // Si la API responde 401 (token expirado o inválido) se cierra la sesión y se vuelve al login.
  useEffect(() => {
    setUnauthorizedHandler(() => {
      borrarSesion();
      queryClient.clear();
      setSesion(null);
      setAviso('Tu sesión expiró o el token no es válido. Inicia sesión nuevamente.');
    });
    return () => setUnauthorizedHandler(null);
  }, [queryClient]);

  function iniciarSesion(nueva: Sesion) {
    guardarSesion(nueva);
    setAviso(null);
    setSesion(nueva);
  }

  function cerrarSesion() {
    borrarSesion();
    queryClient.clear();
    setSesion(null);
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
