import { esReintentable } from '../../../shared/api/errores';
import { EstadoVista } from '../../../shared/components/EstadoVista';
import { useEstudiante } from '../hooks/useEstudiantes';

interface Props {
  id: number;
  nombreCarrera: (carreraId: number) => string;
  onCerrar: () => void;
  /** Solo para ADMIN. */
  onEditar?: () => void;
}

/** Actividad 3 – Opción A: detalle del registro con GET /api/v1/estudiantes/{id}. */
export function EstudianteDetalle({ id, nombreCarrera, onCerrar, onEditar }: Props) {
  const { data, isPending, isError, error, refetch } = useEstudiante(id);

  return (
    <aside className="panel detalle">
      <div className="detalle-header">
        <h2>Detalle del estudiante</h2>
        <button className="secundario" onClick={onCerrar} aria-label="Cerrar detalle">
          ✕
        </button>
      </div>

      {isPending && <EstadoVista tipo="loading" titulo="Cargando detalle…" />}

      {isError && (
        <EstadoVista tipo="error" titulo={error.message}>
          {esReintentable(error) && <button onClick={() => refetch()}>Reintentar</button>}
        </EstadoVista>
      )}

      {data && (
        <dl>
          <dt>ID</dt>
          <dd>{data.id}</dd>
          <dt>Nombre</dt>
          <dd>{data.nombre}</dd>
          <dt>Correo</dt>
          <dd>{data.correo}</dd>
          <dt>Edad</dt>
          <dd>{data.edad} años</dd>
          <dt>Carrera</dt>
          <dd>{nombreCarrera(data.carreraId)}</dd>
        </dl>
      )}

      {data && onEditar && (
        <button className="detalle-editar" onClick={onEditar}>
          Editar
        </button>
      )}
    </aside>
  );
}
