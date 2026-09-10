export type LoginRequest = { email: string; password: string };
export type LoginResponse = { accessToken: string; accessTokenExpiresAt: string };
export type AuthenticatedUser = { id: string; name: string; email: string };
