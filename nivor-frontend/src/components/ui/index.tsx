import type { ButtonHTMLAttributes, HTMLAttributes, InputHTMLAttributes, PointerEvent, ReactNode, TextareaHTMLAttributes } from 'react'
import { cn } from '../../lib/utils'
import { useInViewOnce } from '../../hooks/useMotion'
export function Button({ className, variant = 'primary', ...props }: ButtonHTMLAttributes<HTMLButtonElement> & { variant?: 'primary' | 'secondary' | 'ghost' | 'outline' | 'danger' }) { return <button className={cn('button', `button-${variant}`, className)} {...props} /> }
export function Card({ className, children, onPointerMove, ...props }: HTMLAttributes<HTMLDivElement> & { children: ReactNode }) {
  const { ref, inView } = useInViewOnce<HTMLDivElement>()
  const trackPointer = (event: PointerEvent<HTMLDivElement>) => {
    onPointerMove?.(event)
    if (event.pointerType !== 'mouse' || !window.matchMedia('(hover: hover)').matches) return
    const rect = event.currentTarget.getBoundingClientRect()
    event.currentTarget.style.setProperty('--pointer-x', `${event.clientX - rect.left}px`)
    event.currentTarget.style.setProperty('--pointer-y', `${event.clientY - rect.top}px`)
  }
  return <div ref={ref} className={cn('card', 'motion-reveal', inView && 'is-visible', className)} onPointerMove={trackPointer} {...props}>{children}</div>
}
export function Badge({ children, className }: { children: ReactNode; className?: string }) { return <span className={cn('badge', className)}>{children}</span> }
export function PageContainer({ children, className }: { children: ReactNode; className?: string }) { return <div className={cn('page-container', className)}>{children}</div> }
export function PageHeader({ eyebrow, title, description, action }: { eyebrow?: string; title: string; description?: string; action?: ReactNode }) { return <div className="page-header"><div>{eyebrow && <p className="eyebrow">{eyebrow}</p>}<h1>{title}</h1>{description && <p className="muted">{description}</p>}</div>{action}</div> }
export function ProgressBar({ value, className }: { value: number; className?: string }) {
  const { ref, inView } = useInViewOnce<HTMLDivElement>()
  return <div ref={ref} className={cn('progress-track', className)} role="progressbar" aria-valuenow={value} aria-valuemin={0} aria-valuemax={100}><span style={{ width: inView ? `${Math.max(0, Math.min(100, value))}%` : '0%' }} /></div>
}
export function EmptyState({ title, description, action }: { title: string; description: string; action?: ReactNode }) { return <div className="empty-state"><div className="empty-icon">✳</div><h3>{title}</h3><p>{description}</p>{action}</div> }
export function Divider() { return <div className="divider" /> }
export function IconButton({ label, className, ...props }: ButtonHTMLAttributes<HTMLButtonElement> & { label: string }) { return <button type="button" aria-label={label} className={cn('icon-button', className)} {...props} /> }
export function Input({ className, ...props }: InputHTMLAttributes<HTMLInputElement>) { return <input className={cn('field-input', className)} {...props} /> }
export function Textarea({ className, ...props }: TextareaHTMLAttributes<HTMLTextAreaElement>) { return <textarea className={cn('field-input field-textarea', className)} {...props} /> }
export function Avatar({ initials, className }: { initials: string; className?: string }) { return <span className={cn('avatar', className)} aria-label={`Avatar for ${initials}`}>{initials}</span> }
export function Skeleton({ className }: { className?: string }) { return <span className={cn('skeleton', className)} aria-hidden="true" /> }
export function SkeletonText() { return <div className="skeleton-stack"><Skeleton className="skeleton-line"/><Skeleton className="skeleton-line short"/></div> }
export function SkeletonCard() { return <div className="card skeleton-card"><Skeleton className="skeleton-line"/><SkeletonText/></div> }
export function SkeletonAvatar() { return <Skeleton className="skeleton-avatar"/> }
export function SkeletonList({ rows = 4 }: { rows?: number }) { return <div className="skeleton-list">{Array.from({ length: rows }, (_, i) => <div key={i}><SkeletonAvatar/><SkeletonText/></div>)}</div> }
