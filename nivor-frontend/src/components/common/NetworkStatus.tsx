import { useEffect, useState } from 'react'
import { WifiOff } from 'lucide-react'

export function NetworkStatus() {
  const [online, setOnline] = useState(() => navigator.onLine)
  const [showBackOnline, setShowBackOnline] = useState(false)

  useEffect(() => {
    let timeout = 0
    const offline = () => { window.clearTimeout(timeout); setShowBackOnline(false); setOnline(false) }
    const restored = () => {
      setOnline(true)
      setShowBackOnline(true)
      timeout = window.setTimeout(() => setShowBackOnline(false), 3500)
    }
    window.addEventListener('offline', offline)
    window.addEventListener('online', restored)
    return () => { window.clearTimeout(timeout); window.removeEventListener('offline', offline); window.removeEventListener('online', restored) }
  }, [])

  if (online && !showBackOnline) return null
  return <div className={`network-status ${online ? 'is-online' : ''}`} role="status" aria-live="polite">{!online && <WifiOff size={15}/>} {online ? 'Back online.' : 'You’re offline. Changes may not sync until you reconnect.'}</div>
}
