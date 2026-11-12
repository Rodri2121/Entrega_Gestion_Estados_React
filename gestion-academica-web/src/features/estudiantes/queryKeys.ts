// Claves de consulta del recurso Estudiantes.
// Estructura jerárquica: invalidar `estudiantesKeys.all` refresca la lista y todos los detalles a la vez.
export const estudiantesKeys = {
  all: ['estudiantes'] as const,
  list: () => [...estudiantesKeys.all, 'list'] as const,
  detail: (id: number) => [...estudiantesKeys.all, 'detail', id] as const,
};
