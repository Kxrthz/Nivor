import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuthStore } from '../store/authStore'

export function PublicOnlyRoute() {
  const authenticated = useAuthStore(state => state.isAuthenticated)
  const location = useLocation()
  const from = (location.state as { from?: { pathname?: string } } | null)?.from?.pathname
  return authenticated ? <Navigate to={from && from !== '/login' && from !== '/register' ? from : '/home'} replace /> : <Outlet />
}
