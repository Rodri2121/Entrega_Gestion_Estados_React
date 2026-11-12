import type { Carrera } from '../types';

interface Props {
  id: string;
  value: string;
  onChange: (value: string) => void;
  carreras: Carrera[] | undefined;
  cargando: boolean;
  error: Error | null;
  onReintentar: () => void;
  disabled?: boolean;
  invalido?: boolean;
  describedBy?: string;
}

/** Selector presentacional: recibe el catálogo ya consultado, no hace peticiones. */
export function CarreraSelect({
  id,
  value,
  onChange,
  carreras,
  cargando,
  error,
  onReintentar,
  disabled,
  invalido,
  describedBy,
}: Props) {
  if (error) {
    return (
      <div className="campo-aviso" role="alert">
        No se pudo cargar el catálogo de carreras: {error.message}{' '}
        <button type="button" className="enlace" onClick={onReintentar}>
          Reintentar
        </button>
      </div>
    );
  }

  const vacio = !cargando && carreras !== undefined && carreras.length === 0;

  return (
    <>
      <select
        id={id}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        disabled={disabled || cargando || vacio}
        aria-invalid={invalido || undefined}
        aria-describedby={describedBy}
      >
        <option value="">
          {cargando ? 'Cargando carreras…' : vacio ? 'No hay carreras registradas' : 'Selecciona una carrera'}
        </option>
        {carreras?.map((c) => (
          <option key={c.id} value={String(c.id)}>
            {c.codigo} · {c.nombre}
          </option>
        ))}
      </select>
    </>
  );
}
