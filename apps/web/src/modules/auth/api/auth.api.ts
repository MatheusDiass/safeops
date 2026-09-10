import type { AuthenticatedUser, LoginRequest, LoginResponse } from '../types/auth.types';
import { http } from '../../../shared/api/http';

export async function login(credentials: LoginRequest): Promise<LoginResponse> {
  const response = await http.post<LoginResponse>('/identity/auth/web/login', credentials);

  return response.data;
}

export async function getAuthenticatedUser(accessToken: string): Promise<AuthenticatedUser> {
  const response = await http.get<AuthenticatedUser>('/identity/me', {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return response.data;
}
