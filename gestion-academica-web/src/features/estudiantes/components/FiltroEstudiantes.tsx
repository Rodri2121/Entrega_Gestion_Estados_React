interface Props {
  value: string;
  onChange: (value: string) => void;
  total: number;
  visibles: number;
}

/** Filtro local: solo cambia lo que se muestra; no modifica los datos de la caché ni consulta la API. */
export function FiltroEstudiantes({ value, onChange, total, visibles }: Props) {
  return (
    <div className="filtro">
      <label htmlFor="filtro-estudiantes" className="sr-only">
        Buscar por nombre o correo
      </label>
      <input
        id="filtro-estudiantes"
        type="search"
        placeholder="Buscar por nombre o correo…"
        value={value}
        onChange={(e) => onChange(e.target.value)}
      />
      <span className="filtro-conteo" aria-live="polite">
        {value.trim() ? `${visibles} de ${total}` : `${total} registros`}
      </span>
    </div>
  );
}
