import { useEffect, useRef, useState } from 'react'
import { useReducedMotion } from '../../hooks/useMotion'

export function AnimatedNumber({ value, format, className }: { value: number; format?: (value: number) => string; className?: string }) {
  const [display, setDisplay] = useState(0)
  const current = useRef(0)
  const reduced = useReducedMotion()
  useEffect(() => {
    if (reduced || !Number.isFinite(value)) { current.current = value; setDisplay(value); return }
    const from = current.current
    const start = performance.now()
    let frame = 0
    const tick = (now: number) => {
      const progress = Math.min(1, (now - start) / 480)
      const eased = 1 - Math.pow(1 - progress, 3)
      const next = from + (value - from) * eased
      current.current = next
      setDisplay(next)
      if (progress < 1) frame = requestAnimationFrame(tick)
    }
    frame = requestAnimationFrame(tick)
    return () => cancelAnimationFrame(frame)
  }, [value, reduced])
  const shown = Number.isFinite(display) ? Math.round(display).toLocaleString() : '—'
  return <span className={className} aria-live="off">{format ? format(Number.isFinite(display) ? display : value) : shown}</span>
}
