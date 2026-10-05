export type User = { id: number; name: string; email: string; avatarUrl: string | null; bio: string | null; createdAt: string }
export type AuthResponse = { token: string; user: User }
export type LoginRequest = { email: string; password: string }
export type RegisterRequest = { name: string; email: string; password: string }
export type ApiErrorResponse = { timestamp: string; status: number; error: string; message: string; path: string; fieldErrors?: Record<string, string> }
