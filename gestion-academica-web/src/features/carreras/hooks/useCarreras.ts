import { useQuery } from '@tanstack/react-query';
import { carrerasKeys } from '../queryKeys';
import { listarCarreras } from '../services/carrerasService';

/** Catálogo de carreras (estado remoto). Lo usan la tabla (nombre) y el selector del formulario. */
export function useCarreras() {
  return useQuery({
    queryKey: carrerasKeys.list(),
    queryFn: ({ signal }) => listarCarreras(signal),
    staleTime: 5 * 60 * 1000, // el catálogo cambia poco: se reutiliza la caché 5 minutos
  });
}
