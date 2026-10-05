import { useEffect,useState } from 'react'
import { Link,useLocation } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { ArrowRight,Sparkles } from 'lucide-react'
import { useLogin,useRegister } from '../../hooks/useAuth'
import { getApiErrorMessage } from '../../lib/api'
import { Button } from '../../components/ui'
import type {LoginRequest,RegisterRequest} from '../../types/auth'
type RegisterFields=RegisterRequest&{confirmPassword:string}
export function AuthPage(){
 const path=useLocation().pathname;const login=path==='/login';const loginForm=useForm<LoginRequest>();const registerForm=useForm<RegisterFields>();const signIn=useLogin();const signUp=useRegister();const [error,setError]=useState('')
 useEffect(()=>{const expired=()=>setError('Your session has expired. Please sign in again.');window.addEventListener('nivor:session-expired',expired);return()=>window.removeEventListener('nivor:session-expired',expired)},[])
 const submitLogin=loginForm.handleSubmit(async data=>{setError('');try{await signIn.mutateAsync(data)}catch(e){setError(getApiErrorMessage(e,'Unable to sign in. Check your details and try again.'))}})
 const submitRegister=registerForm.handleSubmit(async data=>{setError('');try{await signUp.mutateAsync({name:data.name,email:data.email,password:data.password})}catch(e){setError(getApiErrorMessage(e,'Unable to create your account. Please try again.'))}})
 const busy=login?signIn.isPending:signUp.isPending
 return <><span className="auth-icon"><Sparkles size={18}/></span><span className="eyebrow">{login?'WELCOME BACK':'A CLEARER START'}</span><h1>{login?'Good to see you.':'Make space for what matters.'}</h1><p>{login?'Sign in to return to your NIVOR space.':'Create your personal space and bring your day into focus.'}</p>
 <form onSubmit={login?submitLogin:submitRegister} className="auth-form" noValidate>
 {!login&&<label>Name<input autoComplete="name" placeholder="Your name" {...registerForm.register('name',{required:'Enter your name.',maxLength:{value:100,message:'Name must be 100 characters or fewer.'}})}/>{registerForm.formState.errors.name&&<small>{registerForm.formState.errors.name.message}</small>}</label>}
 <label>Email address<input autoComplete="email" type="email" placeholder="you@example.com" {...(login?loginForm.register('email',{required:'Enter your email.',pattern:{value:/^[^\s@]+@[^\s@]+\.[^\s@]+$/,message:'Enter a valid email.'}}):registerForm.register('email',{required:'Enter your email.',pattern:{value:/^[^\s@]+@[^\s@]+\.[^\s@]+$/,message:'Enter a valid email.'}}))}/>{(login?loginForm.formState.errors.email:registerForm.formState.errors.email)&&<small>{(login?loginForm.formState.errors.email:registerForm.formState.errors.email)?.message}</small>}</label>
 <label>Password<input autoComplete={login?'current-password':'new-password'} type="password" placeholder="At least 6 characters" {...(login?loginForm.register('password',{required:'Enter your password.'}):registerForm.register('password',{required:'Enter a password.',minLength:{value:6,message:'Use at least 6 characters.'},maxLength:{value:72,message:'Password must be 72 characters or fewer.'}}))}/>{(login?loginForm.formState.errors.password:registerForm.formState.errors.password)&&<small>{(login?loginForm.formState.errors.password:registerForm.formState.errors.password)?.message}</small>}</label>
 {!login&&<label>Confirm password<input autoComplete="new-password" type="password" placeholder="Enter your password again" {...registerForm.register('confirmPassword',{required:'Confirm your password.',validate:value=>value===registerForm.getValues('password')||'Passwords do not match.'})}/>{registerForm.formState.errors.confirmPassword&&<small>{registerForm.formState.errors.confirmPassword.message}</small>}</label>}
 {error&&<div className="auth-error" role="alert">{error}</div>}
 <Button type="submit" disabled={busy}>{busy?'Please wait…':login?'Continue to NIVOR':'Create account'} {!busy&&<ArrowRight size={15}/>}</Button>
 </form><small className="auth-note">{login?'Your session is secured with a private access token.':'Your account data is stored privately in your NIVOR workspace.'}</small><p className="auth-switch">{login?"New to NIVOR? ":'Already have an account? '}<Link to={login?'/register':'/login'}>{login?'Get started':'Sign in'}</Link></p></>
}
