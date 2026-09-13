import axios, { type AxiosError } from 'axios';

type ApiErrorBody = {
  detail?: string;
  message?: string;
};

const apiUrl = import.meta.env.VITE_API_URL ?? 'http://localhost:8000';
const unauthenticatedEndpoints = new Set([
  '/identity/auth/web/login',
  '/identity/auth/web/refresh',
  '/identity/users',
]);
let getAccessToken: (() => string | undefined) | undefined;

export function configureAccessTokenProvider(provider: () => string | undefined): void {
  getAccessToken = provider;
}

export const http = axios.create({
  baseURL: apiUrl,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

http.interceptors.request.use((config) => {
  const accessToken = getAccessToken?.();

  if (accessToken && !unauthenticatedEndpoints.has(config.url ?? '')) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});

http.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ApiErrorBody>) => {
    const message = error.response?.data?.detail ?? error.response?.data?.message;

    return Promise.reject(
      new Error(message ?? 'Unable to sign in. Check your email and password and try again.'),
    );
  },
);
