import { Link, Outlet } from 'react-router-dom'
export function AuthLayout() { return <main className="auth-page"><Link to="/" className="brand"><span className="brand-mark">n</span><span>NIVOR</span></Link><div className="auth-card"><Outlet /></div><p className="muted">A calmer way to move forward.</p></main> }
