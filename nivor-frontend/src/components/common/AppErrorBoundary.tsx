import { Component, type ErrorInfo, type ReactNode } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { AlertTriangle, RotateCcw } from 'lucide-react'

type Props = { children: ReactNode }
type State = { failed: boolean }

class Boundary extends Component<Props & { onHome: () => void }, State> {
  state: State = { failed: false }

  static getDerivedStateFromError(): State { return { failed: true } }

  componentDidCatch(error: Error, info: ErrorInfo) {
    if (import.meta.env.DEV) console.error('NIVOR render error', error, info.componentStack)
  }

  render() {
    if (!this.state.failed) return this.props.children
    return <main className="app-error-page" role="alert"><div className="app-error-card"><span className="app-error-icon"><AlertTriangle size={22}/></span><p className="eyebrow">NIVOR</p><h1>Something went wrong.</h1><p>We couldn’t display this part of your space. Your saved data is safe. Try again, or return to your home page.</p><div className="app-error-actions"><button className="button button-primary" onClick={() => this.setState({ failed: false })}><RotateCcw size={15}/> Try Again</button><Link className="button button-secondary" to="/home" onClick={this.props.onHome}>Go Home</Link></div></div></main>
  }
}

export function AppErrorBoundary({ children }: Props) {
  const navigate = useNavigate()
  return <Boundary onHome={() => navigate('/home', { replace: true })}>{children}</Boundary>
}
