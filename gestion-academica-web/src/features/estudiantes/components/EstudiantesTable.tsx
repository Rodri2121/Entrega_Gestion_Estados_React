import type { Estudiante } from '../types';

interface Props {
  estudiantes: Estudiante[];
  nombreCarrera: (carreraId: number) => string;
  seleccionadoId: number | null;
  onSeleccionar: (id: number) => void;
  /** Solo se pasa si el usuario puede editar (ADMIN); si no, la columna de acciones no se muestra. */
  onEditar?: (id: number) => void;
}

/** Componente presentacional: recibe los datos por props, no hace peticiones. */
export function EstudiantesTable({
  estudiantes,
  nombreCarrera,
  seleccionadoId,
  onSeleccionar,
  onEditar,
}: Props) {
  return (
    <div className="tabla-contenedor">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Correo</th>
            <th>Edad</th>
            <th>Carrera</th>
            {onEditar && <th aria-label="Acciones" />}
          </tr>
        </thead>
        <tbody>
          {estudiantes.map((e) => {
            return (
              <tr
                key={e.id}
                className={e.id === seleccionadoId ? 'seleccionada' : undefined}
                onClick={() => onSeleccionar(e.id)}
                tabIndex={0}
                onKeyDown={(ev) => ev.key === 'Enter' && ev.target === ev.currentTarget && onSeleccionar(e.id)}
                aria-selected={e.id === seleccionadoId}
              >
                <td>{e.id}</td>
                <td>{e.nombre}</td>
                <td>{e.correo}</td>
                <td>{e.edad}</td>
                <td>{nombreCarrera(e.carreraId)}</td>
                {onEditar && (
                  <td className="celda-acciones">
                    <button
                      className="secundario pequeno"
                      onClick={(ev) => {
                        ev.stopPropagation(); // no seleccionar la fila al pulsar Editar
                        onEditar(e.id);
                      }}
                    >
                      Editar
                    </button>
                  </td>
                )}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
