import type { ReactNode } from 'react';

interface Props {
  tipo: 'loading' | 'error' | 'empty';
  titulo: string;
  children?: ReactNode;
}

/** Mensaje reutilizable para los estados de carga, error y ausencia de resultados. */
export function EstadoVista({ tipo, titulo, children }: Props) {
  return (
    <div className={`estado estado-${tipo}`} role={tipo === 'error' ? 'alert' : 'status'}>
      {tipo === 'loading' && <div className="spinner" aria-hidden="true" />}
      <p className="estado-titulo">{titulo}</p>
      {children}
    </div>
  );
}
