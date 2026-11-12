import { useCallback, useEffect, useMemo, useState } from 'react';
import { esProhibido, esReintentable } from '../../../shared/api/errores';
import { useCurrentUser } from '../../../shared/auth/sessionStore';
import { EstadoVista } from '../../../shared/components/EstadoVista';
import { useCarreras } from '../../carreras/hooks/useCarreras';
import { EstudianteDetalle } from '../components/EstudianteDetalle';
import { EstudianteFormDialog, type ModoFormulario } from '../components/EstudianteFormDialog';
import { EstudiantesTable } from '../components/EstudiantesTable';
import { FiltroEstudiantes } from '../components/FiltroEstudiantes';
import { useEstudiantes } from '../hooks/useEstudiantes';

/**
 * Página contenedora.
 *  - Estado REMOTO (TanStack Query): lista de estudiantes y catálogo de carreras.
 *  - Estado de SESIÓN (sessionStore): usuario y rol → qué acciones se muestran.
 *  - Estado LOCAL (useState): texto de búsqueda, fila seleccionada, formulario abierto y aviso de confirmación.
 */
export function EstudiantesPage() {
  const usuario = useCurrentUser();
  const estudiantes = useEstudiantes();
  const carreras = useCarreras();

  const [busqueda, setBusqueda] = useState('');
  const [seleccionadoId, setSeleccionadoId] = useState<number | null>(null);
  const [formulario, setFormulario] = useState<ModoFormulario | null>(null);
  const [confirmacion, setConfirmacion] = useState<string | null>(null);

  const puedeEditar = usuario?.puedeEditarEstudiantes ?? false;

  // Datos derivados: se calculan en el render a partir de la caché, nunca se copian a otro estado.
  const nombreCarrera = (carreraId: number) =>
    carreras.data?.find((c) => c.id === carreraId)?.nombre ?? `Carrera #${carreraId}`;

  const filtrados = useMemo(() => {
    const lista = estudiantes.data ?? [];
    const q = busqueda.trim().toLowerCase();
    if (!q) return lista;
    // filter() devuelve un arreglo nuevo: los datos en caché no se modifican.
    return lista.filter((e) => e.nombre.toLowerCase().includes(q) || e.correo.toLowerCase().includes(q));
  }, [estudiantes.data, busqueda]);

  // La confirmación desaparece sola a los 4 s.
  useEffect(() => {
    if (!confirmacion) return;
    const t = setTimeout(() => setConfirmacion(null), 4000);
    return () => clearTimeout(t);
  }, [confirmacion]);

  const cerrarFormulario = useCallback(() => setFormulario(null), []);
  const onExito = useCallback((mensaje: string) => {
    setFormulario(null);
    setConfirmacion(mensaje);
  }, []);

  return (
    <section>
      <div className="titulo-seccion">
        <div>
          <h2>Estudiantes</h2>
          <p className="subtitulo">
            Datos de <code>GET /api/v1/estudiantes</code>. Selecciona una fila para ver el detalle.
          </p>
        </div>
        <div className="botones">
          <button className="secundario" onClick={() => estudiantes.refetch()} disabled={estudiantes.isFetching}>
            {estudiantes.isFetching ? 'Actualizando…' : 'Actualizar'}
          </button>
          {/* Visible solo para ADMIN. Es una ayuda visual: el backend igual exige ADMIN en POST/PUT. */}
          {puedeEditar && (
            <button onClick={() => setFormulario({ tipo: 'crear' })} disabled={!estudiantes.isSuccess}>
              + Nuevo estudiante
            </button>
          )}
        </div>
      </div>

      {usuario && !puedeEditar && usuario.puedeVerEstudiantes && (
        <p className="info">
          Rol <strong>{usuario.rol}</strong>: modo de solo consulta. El registro y la edición requieren el rol ADMIN.
        </p>
      )}

      {confirmacion && (
        <p className="confirmacion" role="status">
          {confirmacion}
        </p>
      )}

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
          {puedeEditar && <button onClick={() => setFormulario({ tipo: 'crear' })}>Registrar el primero</button>}
        </EstadoVista>
      )}

      {estudiantes.isSuccess && estudiantes.data.length > 0 && (
        <>
          <FiltroEstudiantes
            value={busqueda}
            onChange={setBusqueda}
            total={estudiantes.data.length}
            visibles={filtrados.length}
          />

          {filtrados.length === 0 ? (
            <EstadoVista tipo="empty" titulo={`Ningún estudiante coincide con "${busqueda.trim()}".`}>
              <button className="secundario" onClick={() => setBusqueda('')}>
                Limpiar búsqueda
              </button>
            </EstadoVista>
          ) : (
            <div className={seleccionadoId !== null ? 'contenido con-detalle' : 'contenido'}>
              <EstudiantesTable
                estudiantes={filtrados}
                nombreCarrera={nombreCarrera}
                seleccionadoId={seleccionadoId}
                onSeleccionar={setSeleccionadoId}
                onEditar={puedeEditar ? (id) => setFormulario({ tipo: 'editar', id }) : undefined}
              />
              {seleccionadoId !== null && (
                <EstudianteDetalle
                  id={seleccionadoId}
                  nombreCarrera={nombreCarrera}
                  onCerrar={() => setSeleccionadoId(null)}
                  onEditar={puedeEditar ? () => setFormulario({ tipo: 'editar', id: seleccionadoId }) : undefined}
                />
              )}
            </div>
          )}
        </>
      )}

      {carreras.isError && (
        <p className="aviso">
          No se pudieron cargar los nombres de las carreras: {carreras.error.message}{' '}
          <button className="enlace" onClick={() => carreras.refetch()}>
            Reintentar
          </button>
        </p>
      )}

      {formulario && <EstudianteFormDialog modo={formulario} onCerrar={cerrarFormulario} onExito={onExito} />}
    </section>
  );
}
