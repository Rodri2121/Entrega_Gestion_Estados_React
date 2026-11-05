// Contrato de POST /api/v1/auth/login (LoginRequest / LoginResponse del backend).
export type Rol = 'ADMIN' | 'DOCENTE' | 'ESTUDIANTE';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  username: string;
  rol: Rol;
}
