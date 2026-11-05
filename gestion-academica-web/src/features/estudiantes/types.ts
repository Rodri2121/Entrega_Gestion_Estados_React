// Contrato de GET /api/v1/estudiantes y GET /api/v1/estudiantes/{id} (EstudianteResponse del backend).
export interface Estudiante {
  id: number;
  nombre: string;
  correo: string;
  edad: number;
  carreraId: number;
}
