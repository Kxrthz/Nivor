import { create } from 'zustand'
import { createJSONStorage, persist } from 'zustand/middleware'
import type { User } from '../types/auth'
type AuthState = { user: User | null; token: string | null; isAuthenticated: boolean; setAuth: (token: string, user: User) => void; clearAuth: () => void; setUser: (user: User) => void }
export const useAuthStore = create<AuthState>()(persist((set) => ({ user: null, token: null, isAuthenticated: false, setAuth: (token,user) => set({ token,user,isAuthenticated:true }), clearAuth: () => set({ token:null,user:null,isAuthenticated:false }), setUser: user => set({ user }) }), { name:'nivor-auth', storage:createJSONStorage(() => sessionStorage), partialize: state => ({ token:state.token,user:state.user,isAuthenticated:Boolean(state.token) }) as AuthState }))
