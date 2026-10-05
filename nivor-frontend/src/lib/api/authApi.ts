import { api } from '../api'
import type { AuthResponse, LoginRequest, RegisterRequest, User } from '../../types/auth'
export const authApi = { login: async (body:LoginRequest) => (await api.post<AuthResponse>('/auth/login',body)).data, register: async (body:RegisterRequest) => (await api.post<AuthResponse>('/auth/register',body)).data, me: async () => (await api.get<User>('/auth/me')).data, logout: async () => { await api.post('/auth/logout') } }
