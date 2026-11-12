// TanStack Query gestiona el ESTADO REMOTO: caché, carga/error, cancelación (signal) y reintento.
// Los componentes nunca copian estos datos a un useState propio: la caché es la única fuente de verdad en el cliente.
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { estudiantesKeys } from '../queryKeys';
import {
  actualizarEstudiante,
  crearEstudiante,
  listarEstudiantes,
  obtenerEstudiante,
} from '../services/estudiantesService';
import type { Estudiante, EstudianteRequest } from '../types';

export function useEstudiantes(enabled = true) {
  return useQuery({
    queryKey: estudiantesKeys.list(),
    queryFn: ({ signal }) => listarEstudiantes(signal),
    enabled,
  });
}

export function useEstudiante(id: number | null) {
  return useQuery({
    queryKey: estudiantesKeys.detail(id ?? -1),
    queryFn: ({ signal }) => obtenerEstudiante(id as number, signal),
    enabled: id !== null, // solo consulta cuando hay un registro seleccionado
  });
}

/**
 * Registrar estudiante.
 * Al confirmar éxito: (1) se agrega la respuesta del servidor a la lista en caché para que la tabla
 * se actualice al instante, y (2) se invalida la lista para que TanStack Query la vuelva a pedir en
 * segundo plano y quede igual que en la base de datos. Sin window.location.reload().
 */
export function useCrearEstudiante() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (datos: EstudianteRequest) => crearEstudiante(datos),
    onSuccess: (creado) => {
      queryClient.setQueryData<Estudiante[]>(estudiantesKeys.list(), (prev) =>
        prev ? [...prev, creado] : prev,
      );
      queryClient.setQueryData(estudiantesKeys.detail(creado.id), creado);
      return queryClient.invalidateQueries({ queryKey: estudiantesKeys.list() });
    },
  });
}

/**
 * Editar estudiante.
 * Al confirmar éxito se reemplaza el registro en la lista y en su detalle (si estaba abierto) y luego
 * se invalidan todas las consultas de estudiantes para sincronizar con el servidor.
 */
export function useActualizarEstudiante() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, datos }: { id: number; datos: EstudianteRequest }) => actualizarEstudiante(id, datos),
    onSuccess: (actualizado) => {
      queryClient.setQueryData<Estudiante[]>(estudiantesKeys.list(), (prev) =>
        prev?.map((e) => (e.id === actualizado.id ? actualizado : e)),
      );
      queryClient.setQueryData(estudiantesKeys.detail(actualizado.id), actualizado);
      return queryClient.invalidateQueries({ queryKey: estudiantesKeys.all });
    },
  });
}
