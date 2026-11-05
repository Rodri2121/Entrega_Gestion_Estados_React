import { QueryClient } from '@tanstack/react-query';
import { ApiError } from '../shared/api/httpClient';

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // Reintentar 400/401/403/404 no cambia el resultado; solo se reintenta una vez ante fallas de red o 5xx.
      retry: (failureCount, error) => {
        if (error instanceof ApiError && error.status !== undefined && error.status < 500) return false;
        return failureCount < 1;
      },
      refetchOnWindowFocus: false,
    },
  },
});
