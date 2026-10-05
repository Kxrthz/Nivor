import { useEffect } from 'react'
import { useMutation,useQuery,useQueryClient } from '@tanstack/react-query'
import { useLocation,useNavigate } from 'react-router-dom'
import { authApi } from '../lib/api/authApi'
import { useAuthStore } from '../store/authStore'
import { queryKeys } from '../lib/queryKeys'
import type { LoginRequest,RegisterRequest } from '../types/auth'
export function useCurrentUser(){const authenticated=useAuthStore(s=>s.isAuthenticated);const setUser=useAuthStore(s=>s.setUser);const query=useQuery({queryKey:queryKeys.me,queryFn:authApi.me,enabled:authenticated,staleTime:60_000});useEffect(()=>{if(query.data)setUser(query.data)},[query.data,setUser]);return query}
export function useLogin(){const q=useQueryClient();const navigate=useNavigate();const location=useLocation();const setAuth=useAuthStore(s=>s.setAuth);return useMutation({mutationFn:(data:LoginRequest)=>authApi.login(data),onSuccess:result=>{setAuth(result.token,result.user);q.setQueryData(queryKeys.me,result.user);const from=(location.state as {from?:{pathname?:string}}|null)?.from?.pathname;navigate(from&&from!=='/login'?from:'/home',{replace:true})}})}
export function useRegister(){const q=useQueryClient();const navigate=useNavigate();const setAuth=useAuthStore(s=>s.setAuth);return useMutation({mutationFn:(data:RegisterRequest)=>authApi.register(data),onSuccess:result=>{setAuth(result.token,result.user);q.setQueryData(queryKeys.me,result.user);navigate('/home',{replace:true})}})}
export function useLogout(){const q=useQueryClient();const clear=useAuthStore(s=>s.clearAuth);return async()=>{try{await authApi.logout()}finally{clear();q.clear()}}}
