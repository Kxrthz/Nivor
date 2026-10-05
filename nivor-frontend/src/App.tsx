import { useEffect } from 'react'
import { AppRoutes } from './routes/AppRoutes'
import { AppErrorBoundary } from './components/common/AppErrorBoundary'
import { NetworkStatus } from './components/common/NetworkStatus'
import { useReducedMotion } from './hooks/useMotion'
function AmbientBackground() {
  const reducedMotion = useReducedMotion()
  useEffect(() => {
    const root = document.documentElement
    let frame = 0
    const update = () => {
      cancelAnimationFrame(frame)
      frame = requestAnimationFrame(() => {
        const range = Math.max(1, root.scrollHeight - window.innerHeight)
        const progress = reducedMotion ? 0 : Math.max(0, Math.min(1, window.scrollY / range))
        root.style.setProperty('--scroll-progress', progress.toFixed(4))
        root.style.setProperty('--ambient-scroll', `${(progress - 0.5) * 42}px`)
        root.style.setProperty('--ambient-scroll-reverse', `${(0.5 - progress) * 24}px`)
        root.style.setProperty('--decorative-scroll', `${(progress - 0.5) * -14}px`)
      })
    }
    window.addEventListener('scroll', update, { passive: true })
    window.addEventListener('resize', update, { passive: true })
    update()
    return () => {
      window.removeEventListener('scroll', update); window.removeEventListener('resize', update); cancelAnimationFrame(frame)
      root.style.removeProperty('--scroll-progress'); root.style.removeProperty('--ambient-scroll'); root.style.removeProperty('--ambient-scroll-reverse'); root.style.removeProperty('--decorative-scroll')
    }
  }, [reducedMotion])
  return <div className="ambient-background" aria-hidden="true"><span className="ambient-glow ambient-glow-violet"/><span className="ambient-glow ambient-glow-cyan"/></div>
}
export default function App() { return <AppErrorBoundary><AmbientBackground/><NetworkStatus/><AppRoutes/></AppErrorBoundary> }
