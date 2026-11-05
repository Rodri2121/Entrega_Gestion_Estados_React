import type { Estudiante } from '../types';

interface Props {
  estudiantes: Estudiante[];
  nombreCarrera: (carreraId: number) => string;
  seleccionadoId: number | null;
  onSeleccionar: (id: number) => void;
}

export function EstudiantesTable({ estudiantes, nombreCarrera, seleccionadoId, onSeleccionar }: Props) {
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
          </tr>
        </thead>
        <tbody>
          {estudiantes.map((e) => (
            <tr
              key={e.id}
              className={e.id === seleccionadoId ? 'seleccionada' : undefined}
              onClick={() => onSeleccionar(e.id)}
              tabIndex={0}
              onKeyDown={(ev) => ev.key === 'Enter' && onSeleccionar(e.id)}
              aria-selected={e.id === seleccionadoId}
            >
              <td>{e.id}</td>
              <td>{e.nombre}</td>
              <td>{e.correo}</td>
              <td>{e.edad}</td>
              <td>{nombreCarrera(e.carreraId)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
