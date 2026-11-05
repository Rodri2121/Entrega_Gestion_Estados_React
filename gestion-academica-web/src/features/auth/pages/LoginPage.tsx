import { useState, type FormEvent } from 'react';
import { useMutation } from '@tanstack/react-query';
import { ApiError } from '../../../shared/api/httpClient';
import type { Sesion } from '../../../shared/auth/tokenStorage';
import { login } from '../services/authService';

interface Props {
  aviso?: string | null;
  onLogin: (sesion: Sesion) => void;
}

export function LoginPage({ aviso, onLogin }: Props) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const mutation = useMutation({
    mutationFn: login,
    onSuccess: (res) => onLogin({ accessToken: res.accessToken, username: res.username, rol: res.rol }),
  });

  function onSubmit(e: FormEvent) {
    e.preventDefault();
    mutation.mutate({ username: username.trim(), password });
  }

  const error = mutation.error instanceof ApiError ? mutation.error : null;
  const mensajeError =
    error?.status === 401 ? 'Usuario o contraseña incorrectos.' : error?.message ?? (mutation.error ? 'Error inesperado.' : null);

  return (
    <main className="login">
      <form className="panel login-panel" onSubmit={onSubmit}>
        <h1>Gestión Académica</h1>
        <p className="subtitulo">Inicia sesión para consultar la API</p>

        {aviso && <p className="aviso">{aviso}</p>}

        <label>
          Usuario
          <input value={username} onChange={(e) => setUsername(e.target.value)} autoComplete="username" required />
        </label>
        <label>
          Contraseña
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="current-password"
            required
          />
        </label>

        {mensajeError && (
          <p className="error-texto" role="alert">
            {mensajeError}
          </p>
        )}

        <button type="submit" disabled={mutation.isPending}>
          {mutation.isPending ? 'Ingresando…' : 'Ingresar'}
        </button>
      </form>
    </main>
  );
}
