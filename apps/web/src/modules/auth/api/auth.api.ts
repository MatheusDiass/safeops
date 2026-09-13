import type {
  AuthenticatedUser,
  LoginRequest,
  LoginResponse,
  RegisterUserRequest,
} from '../types/auth.types';
import { http } from '../../../shared/api/http';

export async function login(credentials: LoginRequest): Promise<LoginResponse> {
  const response = await http.post<LoginResponse>('/identity/auth/web/login', credentials);

  return response.data;
}

export async function refreshSession(): Promise<LoginResponse> {
  const response = await http.post<LoginResponse>('/identity/auth/web/refresh');

  return response.data;
}

export async function logoutSession(): Promise<void> {
  await http.post('/identity/auth/web/logout');
}

export async function registerUser(request: RegisterUserRequest): Promise<void> {
  await http.post('/identity/users', request);
}

export async function getAuthenticatedUser(): Promise<AuthenticatedUser> {
  const response = await http.get<AuthenticatedUser>('/identity/me');

  return response.data;
}
