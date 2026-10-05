import axios from 'axios'
import { useAuthStore } from '../store/authStore'
import { queryClient } from './queryClient'

const apiBaseUrl = import.meta.env.VITE_API_URL?.trim().replace(/\/+$/, '') || (import.meta.env.DEV ? 'http://localhost:8080/api' : '/api')
export const api = axios.create({ baseURL: apiBaseUrl, timeout: 15_000, headers: { 'Content-Type': 'application/json' } })
api.interceptors.request.use(config => { const token = useAuthStore.getState().token; if (token) config.headers.Authorization = `Bearer ${token}`; return config })
api.interceptors.response.use(response => response, error => {
  if (error?.response?.status === 401 && !String(error.config?.url ?? '').match(/\/auth\/(login|register)$/)) {
    useAuthStore.getState().clearAuth(); queryClient.clear();
    if (typeof window !== 'undefined') window.dispatchEvent(new CustomEvent('nivor:session-expired'))
  }
  return Promise.reject(error)
})
export function getApiErrorMessage(error: unknown, fallback = 'Something went wrong. Please try again.') {
  if (axios.isAxiosError(error)) {
    const status = error.response?.status
    const data = error.response?.data as { message?: string; fieldErrors?: Record<string,string> } | undefined
    const fieldError = Object.values(data?.fieldErrors ?? {}).find(value => typeof value === 'string' && value.trim())
    if (fieldError) return fieldError
    if (status === 400 || status === 422) return data?.message && data.message.length < 240 ? data.message : 'Check the information and try again.'
    if (status === 401) return /\/auth\/login$/.test(String(error.config?.url ?? '')) ? 'Email or password is incorrect.' : 'Your session has expired. Please sign in again.'
    if (status === 403) return 'You don’t have permission to do that.'
    if (status === 404) return 'We couldn’t find what you requested.'
    if (status === 409) return data?.message && data.message.length < 240 ? data.message : 'This conflicts with existing information.'
    if (status === 429) return 'You’re doing that a little too quickly. Please wait and try again.'
    if (status && status >= 500) return 'NIVOR is temporarily unavailable. Please try again shortly.'
    if (!error.response) return typeof navigator !== 'undefined' && !navigator.onLine ? 'You’re offline. Reconnect and try again.' : 'Could not reach NIVOR. Please try again.'
    return fallback
  }
  return fallback
}
