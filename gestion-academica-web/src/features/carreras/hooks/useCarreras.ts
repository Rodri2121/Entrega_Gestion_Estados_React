import { useQuery } from '@tanstack/react-query';
import { listarCarreras } from '../services/carrerasService';

export function useCarreras() {
  return useQuery({
    queryKey: ['carreras'],
    queryFn: ({ signal }) => listarCarreras(signal),
    staleTime: 5 * 60 * 1000, // las carreras cambian poco
  });
}
