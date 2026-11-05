import { httpClient } from '../../../shared/api/httpClient';
import type { LoginRequest, LoginResponse } from '../types';

export function login(credenciales: LoginRequest): Promise<LoginResponse> {
  return httpClient.post<LoginResponse>('/auth/login', credenciales, { auth: false });
}
