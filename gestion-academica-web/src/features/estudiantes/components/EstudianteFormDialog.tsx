import { useEffect, type ReactNode } from 'react';
import { esReintentable } from '../../../shared/api/errores';
import { EstadoVista } from '../../../shared/components/EstadoVista';
import { useCarreras } from '../../carreras/hooks/useCarreras';
import { useActualizarEstudiante, useCrearEstudiante, useEstudiante } from '../hooks/useEstudiantes';
import type { Estudiante } from '../types';
import { VALORES_VACIOS, type ValoresFormulario } from '../validation/estudianteValidation';
import { EstudianteForm } from './EstudianteForm';

export type ModoFormulario = { tipo: 'crear' } | { tipo: 'editar'; id: number };

interface Props {
  modo: ModoFormulario;
  onCerrar: () => void;
  onExito: (mensaje: string) => void;
}

function aValores(e: Estudiante): ValoresFormulario {
  return { nombre: e.nombre, correo: e.correo, edad: String(e.edad), carrera: String(e.carreraId) };
}

/**
 * Contenedor del formulario: conecta los hooks (queries y mutaciones) con el formulario presentacional.
 * Las peticiones viven aquí y en los hooks, nunca en EstudianteForm ni en CarreraSelect.
 */
export function EstudianteFormDialog({ modo, onCerrar, onExito }: Props) {
  const carreras = useCarreras();
  const crear = useCrearEstudiante();
  const actualizar = useActualizarEstudiante();
  // Edición: se cargan los datos existentes con GET /estudiantes/{id}.
  const existente = useEstudiante(modo.tipo === 'editar' ? modo.id : null);
  const pendiente = crear.isPending || actualizar.isPending;

  // Escape cierra el diálogo (salvo mientras se guarda).
  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      if (e.key === 'Escape' && !pendiente) onCerrar();
    };
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [pendiente, onCerrar]);

  const carrerasProps = {
    data: carreras.data,
    cargando: carreras.isPending,
    error: carreras.error,
    reintentar: () => void carreras.refetch(),
  };

  let contenido: ReactNode;
  if (modo.tipo === 'crear') {
    contenido = (
      <EstudianteForm
        valoresIniciales={VALORES_VACIOS}
        textoEnviar="Registrar"
        pendiente={pendiente}
        onCancelar={onCerrar}
        carreras={carrerasProps}
        onGuardar={async (datos) => {
          const creado = await crear.mutateAsync(datos);
          onExito(`Estudiante "${creado.nombre}" registrado correctamente.`);
        }}
      />
    );
  } else if (existente.isPending) {
    contenido = <EstadoVista tipo="loading" titulo="Cargando datos del estudiante…" />;
  } else if (existente.isError) {
    contenido = (
      <EstadoVista tipo="error" titulo={existente.error.message}>
        {esReintentable(existente.error) && <button onClick={() => existente.refetch()}>Reintentar</button>}
      </EstadoVista>
    );
  } else {
    const id = modo.id;
    contenido = (
      <EstudianteForm
        key={id}
        valoresIniciales={aValores(existente.data)}
        textoEnviar="Guardar cambios"
        pendiente={pendiente}
        onCancelar={onCerrar}
        carreras={carrerasProps}
        onGuardar={async (datos) => {
          const actualizado = await actualizar.mutateAsync({ id, datos });
          onExito(`Cambios de "${actualizado.nombre}" guardados.`);
        }}
      />
    );
  }

  return (
    <div className="overlay" onMouseDown={(e) => e.target === e.currentTarget && !pendiente && onCerrar()}>
      <div className="panel dialogo" role="dialog" aria-modal="true" aria-labelledby="titulo-dialogo">
        <div className="detalle-header">
          <h2 id="titulo-dialogo">{modo.tipo === 'crear' ? 'Registrar estudiante' : `Editar estudiante #${modo.id}`}</h2>
          <button className="secundario" onClick={onCerrar} disabled={pendiente} aria-label="Cerrar">
            ✕
          </button>
        </div>
        <p className="subtitulo">
          <code>{modo.tipo === 'crear' ? 'POST /api/v1/estudiantes' : `PUT /api/v1/estudiantes/${modo.id}`}</code>
        </p>
        {contenido}
      </div>
    </div>
  );
}
