// Contrato de GET /api/v1/estudiantes y GET /api/v1/estudiantes/{id} (EstudianteResponse del backend).
export interface Estudiante {
  id: number;
  nombre: string;
  correo: string;
  edad: number;
  carreraId: number;
}

// Contrato de POST /api/v1/estudiantes y PUT /api/v1/estudiantes/{id}
// (EstudianteRequest / EstudianteUpdateRequest). Ojo: la API recibe `carrera`, pero responde `carreraId`.
export interface EstudianteRequest {
  nombre: string;
  correo: string;
  edad: number;
  carrera: number;
}

export type CampoEstudiante = keyof EstudianteRequest;
