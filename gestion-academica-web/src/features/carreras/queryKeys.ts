// Claves de consulta del recurso Carreras (separadas de las de estudiantes).
export const carrerasKeys = {
  all: ['carreras'] as const,
  list: () => [...carrerasKeys.all, 'list'] as const,
};
