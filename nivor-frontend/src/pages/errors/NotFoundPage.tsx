import { Link } from 'react-router-dom'
import { ArrowLeft, Sparkles } from 'lucide-react'
export function NotFoundPage() { return <main className="not-found"><span className="brand-mark">n</span><span className="eyebrow">LOST IN THE SYSTEM</span><strong>404</strong><h1>This page doesn't exist.</h1><p>Looks like this path hasn't found its place yet.</p><Link to="/" className="button button-primary"><ArrowLeft size={15}/> Return home</Link><Sparkles className="not-found-spark" size={19}/></main> }
