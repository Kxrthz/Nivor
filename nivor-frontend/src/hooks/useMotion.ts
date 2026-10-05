import { useEffect, useRef, useState, useSyncExternalStore } from 'react'

export const motionTokens = { fast: 0.12, micro: 0.16, normal: 0.22, ui: 0.3, cinematic: 0.55, ambient: 48 } as const

let motionQuery: MediaQueryList | undefined
const subscribers = new Set<() => void>()
const notifyMotionSubscribers = () => subscribers.forEach(subscriber => subscriber())
function subscribeToMotionPreference(subscriber: () => void) {
  motionQuery ??= window.matchMedia('(prefers-reduced-motion: reduce)')
  subscribers.add(subscriber)
  if (subscribers.size === 1) motionQuery.addEventListener('change', notifyMotionSubscribers)
  return () => {
    subscribers.delete(subscriber)
    if (subscribers.size === 0) motionQuery?.removeEventListener('change', notifyMotionSubscribers)
  }
}
const getMotionPreference = () => typeof window !== 'undefined' && window.matchMedia('(prefers-reduced-motion: reduce)').matches

export function useReducedMotion() { return useSyncExternalStore(subscribeToMotionPreference, getMotionPreference, () => false) }

export function useInViewOnce<T extends HTMLElement>() {
  const ref = useRef<T>(null)
  const [inView, setInView] = useState(false)
  const reduced = useReducedMotion()
  useEffect(() => {
    const element = ref.current
    if (!element || reduced || !('IntersectionObserver' in window)) { setInView(true); return }
    const observer = new IntersectionObserver(entries => {
      if (entries.some(entry => entry.isIntersecting)) { setInView(true); observer.disconnect() }
    }, { threshold: 0.08, rootMargin: '0px 0px -24px 0px' })
    observer.observe(element)
    return () => observer.disconnect()
  }, [reduced])
  return { ref, inView }
}
