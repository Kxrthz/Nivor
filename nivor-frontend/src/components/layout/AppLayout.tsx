import { useEffect, useState } from 'react'
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom'
import { AnimatePresence, motion } from 'framer-motion'
import { Activity, BookOpen, CalendarDays, ChartNoAxesCombined, CheckSquare, ChevronLeft, Command, Compass, FileText, FolderKanban, HeartPulse, Home, LogOut, Menu, Search, Settings, Sparkles, Target, Timer, UserRound, Wallet, X } from 'lucide-react'
import { useUIStore } from '../../store/uiStore'
import { CommandPalette } from './CommandPalette'
import { NotificationBell } from './NotificationBell'
import { useLogout } from '../../hooks/useAuth'

const groups = [
  { label: 'CORE', links: [{ label: 'Home', to: '/home', icon: Home }, { label: 'Planner', to: '/planner', icon: CalendarDays }, { label: 'NIVOR AI', to: '/ai', icon: Sparkles }] },
  { label: 'PRODUCTIVITY', links: [{ label: 'Workspace', to: '/workspace', icon: FolderKanban }, { label: 'Notes', to: '/notes', icon: FileText }, { label: 'Tasks', to: '/tasks', icon: CheckSquare }, { label: 'Goals', to: '/goals', icon: Target }, { label: 'Habits', to: '/habits', icon: Activity }, { label: 'Journal', to: '/journal', icon: BookOpen }] },
  { label: 'SYSTEMS', links: [{ label: 'Files', to: '/files', icon: FolderKanban }, { label: 'Finance', to: '/finance', icon: Wallet }, { label: 'Health', to: '/health', icon: HeartPulse }, { label: 'Focus', to: '/focus', icon: Timer }, { label: 'Analytics', to: '/analytics', icon: ChartNoAxesCombined }] },
]
const titleFor = (path: string) => path === '/' ? 'Home' : path.slice(1).split('/').map(s => s[0]?.toUpperCase() + s.slice(1)).join(' / ')
const atmosphereFor = (path: string) => {
  const route = path.split('/')[1] ?? 'home'
  return ['home','planner','ai','workspace','notes','tasks','goals','habits','journal','files','finance','health','focus','analytics'].includes(route) ? route : 'default'
}

export function AppLayout() {
  const { sidebarCollapsed, toggleSidebar, theme, setTheme, accent, setAccent } = useUIStore()
  const [searchOpen, setSearchOpen] = useState(false)
  const [mobileOpen, setMobileOpen] = useState(false)
  const location = useLocation()
  const logout = useLogout()
  useEffect(() => {
    const key = (event: KeyboardEvent) => { if ((event.ctrlKey || event.metaKey) && event.key.toLowerCase() === 'k') { event.preventDefault(); setSearchOpen(true) } }
    window.addEventListener('keydown', key); return () => window.removeEventListener('keydown', key)
  }, [])
  useEffect(() => { setMobileOpen(false) }, [location.pathname])
  useEffect(() => {
    document.documentElement.dataset.atmosphere = atmosphereFor(location.pathname)
  }, [location.pathname])
  useEffect(() => () => { delete document.documentElement.dataset.atmosphere }, [])
  useEffect(() => {
    const media = window.matchMedia('(prefers-color-scheme: light)')
    const applyTheme = () => { document.documentElement.dataset.theme = theme === 'system' ? (media.matches ? 'light' : 'dark') : theme }
    applyTheme(); document.documentElement.dataset.accent = accent
    media.addEventListener('change', applyTheme)
    return () => media.removeEventListener('change', applyTheme)
  }, [theme, accent])
  return <div className="app-shell">
    <motion.aside className={`sidebar ${sidebarCollapsed ? 'is-collapsed' : ''}`} animate={{ width: sidebarCollapsed ? 76 : 254 }} transition={{ duration: .22, ease: 'easeOut' }}>
      <Link to="/home" className="brand"><span className="brand-mark">n</span>{!sidebarCollapsed && <span>NIVOR</span>}</Link>
      <button className="collapse-button" onClick={toggleSidebar} aria-label="Toggle sidebar"><ChevronLeft size={15} /></button>
      <nav className="side-nav">{groups.map(group => <div className="nav-group" key={group.label}>{!sidebarCollapsed && <p className="nav-caption">{group.label}</p>}{group.links.map(({ label, to, icon: Icon }) => <NavLink key={to} to={to} title={sidebarCollapsed ? label : undefined} className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}><Icon size={17} strokeWidth={1.8} />{!sidebarCollapsed && <span>{label}</span>}{label === 'NIVOR AI' && !sidebarCollapsed && <span className="nav-spark">✦</span>}</NavLink>)}</div>)}</nav>
      <div className="sidebar-bottom"><NavLink to="/profile" className={({ isActive }) => `profile-mini ${isActive ? 'active' : ''}`}><span className="avatar">A</span>{!sidebarCollapsed && <span className="profile-copy"><strong>My profile</strong><small>Personal space</small></span>}</NavLink><NavLink to="/settings" title="Settings" className="nav-link"><Settings size={17}/>{!sidebarCollapsed && <span>Settings</span>}</NavLink><button title="Sign out" onClick={() => void logout()} className="nav-link logout-link"><LogOut size={17}/>{!sidebarCollapsed && <span>Sign out</span>}</button></div>
    </motion.aside>
    <div className="app-main"><header className="topbar"><div className="top-left"><button className="mobile-menu icon-button" onClick={() => setMobileOpen(!mobileOpen)} aria-label="Open navigation">{mobileOpen ? <X size={19}/> : <Menu size={19}/>}</button><span className="current-page">{titleFor(location.pathname)}</span><span className="breadcrumb-dot">/</span><span className="breadcrumb-muted">Personal space</span></div><div className="top-actions"><button className="search-trigger" onClick={() => setSearchOpen(true)}><Search size={15}/><span>Search anything...</span><kbd><Command size={10}/> K</kbd></button><NotificationBell/><label className="sr-only" htmlFor="theme-choice">Theme</label><select id="theme-choice" className="theme-select" value={theme} onChange={e => setTheme(e.target.value as typeof theme)} aria-label="Theme"><option value="dark">Dark</option><option value="light">Light</option><option value="system">System</option></select><label className="sr-only" htmlFor="accent-choice">Accent color</label><select id="accent-choice" className="accent-select" value={accent} onChange={e => setAccent(e.target.value as typeof accent)} aria-label="Accent color"><option value="violet">Violet</option><option value="cyan">Cyan</option><option value="emerald">Emerald</option><option value="amber">Amber</option></select><Link to="/profile" className="avatar top-avatar" aria-label="Profile">A</Link></div></header>
      <main className="main-content"><AnimatePresence mode="wait"><motion.div key={location.pathname} initial={{ opacity: 0, y: 8, scale: .995 }} animate={{ opacity: 1, y: 0, scale: 1 }} exit={{ opacity: 0, y: -3, scale: .998 }} transition={{ duration: .24, ease: [.2,.75,.25,1] }}><Outlet /></motion.div></AnimatePresence></main>
    </div>
    <nav className="mobile-bottom" aria-label="Mobile navigation">{[{ to: '/home', label: 'Home', Icon: Home }, { to: '/planner', label: 'Planner', Icon: CalendarDays }, { to: '/ai', label: 'AI', Icon: Sparkles }, { to: '/workspace', label: 'Space', Icon: Compass }, { to: '/profile', label: 'Profile', Icon: UserRound }].map(({ to, label, Icon }) => <NavLink key={to} to={to} className={({ isActive }) => `mobile-nav-item ${isActive ? 'active' : ''} ${label === 'AI' ? 'mobile-ai' : ''}`}><Icon size={label === 'AI' ? 20 : 18}/><span>{label}</span></NavLink>)}</nav>
    <AnimatePresence>{mobileOpen && <><motion.button className="mobile-scrim" aria-label="Close menu" onClick={() => setMobileOpen(false)} initial={{opacity:0}} animate={{opacity:1}} exit={{opacity:0}}/><motion.aside className="mobile-drawer" initial={{x:-300}} animate={{x:0}} exit={{x:-300}}>{groups.flatMap(g => g.links).map(({ label, to, icon: Icon }) => <NavLink key={to} to={to} className="nav-link"><Icon size={18}/>{label}</NavLink>)}</motion.aside></>}</AnimatePresence>
    <CommandPalette open={searchOpen} onClose={() => setSearchOpen(false)} />
  </div>
}
