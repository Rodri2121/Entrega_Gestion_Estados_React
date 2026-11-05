// TanStack Query (incluido en el proyecto) gestiona caché, estados de carga/error,
// cancelación (signal) y reintento sin escribir useEffect manualmente.
import { useQuery } from '@tanstack/react-query';
import { listarEstudiantes, obtenerEstudiante } from '../services/estudiantesService';

export function useEstudiantes() {
  return useQuery({
    queryKey: ['estudiantes'],
    queryFn: ({ signal }) => listarEstudiantes(signal),
  });
}

export function useEstudiante(id: number | null) {
  return useQuery({
    queryKey: ['estudiantes', id],
    queryFn: ({ signal }) => obtenerEstudiante(id as number, signal),
    enabled: id !== null, // solo consulta cuando hay un registro seleccionado
  });
}
