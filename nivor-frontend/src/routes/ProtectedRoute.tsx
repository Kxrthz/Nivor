import { Navigate,Outlet,useLocation } from 'react-router-dom'
import { useAuthStore } from '../store/authStore'
export function ProtectedRoute(){const authenticated=useAuthStore(s=>s.isAuthenticated);const location=useLocation();return authenticated?<Outlet/>:<Navigate to="/login" replace state={{from:location}}/>}
