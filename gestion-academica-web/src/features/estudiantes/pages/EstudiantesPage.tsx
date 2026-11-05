import { useState } from 'react';
import { esProhibido, esReintentable } from '../../../shared/api/errores';
import { EstadoVista } from '../../../shared/components/EstadoVista';
import { useCarreras } from '../../carreras/hooks/useCarreras';
import { EstudianteDetalle } from '../components/EstudianteDetalle';
import { EstudiantesTable } from '../components/EstudiantesTable';
import { useEstudiantes } from '../hooks/useEstudiantes';

export function EstudiantesPage() {
  const estudiantes = useEstudiantes();
  const carreras = useCarreras();
  const [seleccionadoId, setSeleccionadoId] = useState<number | null>(null);

  // Dato derivado: se calcula en el render, no se guarda en estado.
  const nombreCarrera = (carreraId: number) =>
    carreras.data?.find((c) => c.id === carreraId)?.nombre ?? `Carrera #${carreraId}`;

  return (
    <section>
      <div className="titulo-seccion">
        <div>
          <h2>Estudiantes</h2>
          <p className="subtitulo">
            Datos de <code>GET /api/v1/estudiantes</code>. Selecciona una fila para ver el detalle.
          </p>
        </div>
        <button className="secundario" onClick={() => estudiantes.refetch()} disabled={estudiantes.isFetching}>
          {estudiantes.isFetching ? 'Actualizando…' : 'Actualizar'}
        </button>
      </div>

      {estudiantes.isPending && <EstadoVista tipo="loading" titulo="Cargando estudiantes…" />}

      {estudiantes.isError && (
        <EstadoVista tipo="error" titulo={estudiantes.error.message}>
          {esProhibido(estudiantes.error) && (
            <p>La consulta de estudiantes solo está permitida para los roles ADMIN y DOCENTE.</p>
          )}
          {esReintentable(estudiantes.error) && <button onClick={() => estudiantes.refetch()}>Reintentar</button>}
        </EstadoVista>
      )}

      {estudiantes.isSuccess && estudiantes.data.length === 0 && (
        <EstadoVista tipo="empty" titulo="No hay estudiantes registrados.">
          <p>La consulta funcionó correctamente, pero la API devolvió una lista vacía.</p>
        </EstadoVista>
      )}

      {estudiantes.isSuccess && estudiantes.data.length > 0 && (
        <div className={seleccionadoId !== null ? 'contenido con-detalle' : 'contenido'}>
          <EstudiantesTable
            estudiantes={estudiantes.data}
            nombreCarrera={nombreCarrera}
            seleccionadoId={seleccionadoId}
            onSeleccionar={setSeleccionadoId}
          />
          {seleccionadoId !== null && (
            <EstudianteDetalle id={seleccionadoId} nombreCarrera={nombreCarrera} onCerrar={() => setSeleccionadoId(null)} />
          )}
        </div>
      )}

      {carreras.isError && (
        <p className="aviso">No se pudieron cargar los nombres de las carreras: {carreras.error.message}</p>
      )}
    </section>
  );
}
