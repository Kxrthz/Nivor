import { create } from 'zustand'
import { persist } from 'zustand/middleware'
export type ThemePreference = 'dark' | 'light' | 'system'
export type AccentName = 'violet' | 'cyan' | 'emerald' | 'amber'
type UIState = { sidebarCollapsed: boolean; theme: ThemePreference; accent: AccentName; focusMinutes: number; toggleSidebar: () => void; setTheme: (theme: ThemePreference) => void; setAccent: (accent: AccentName) => void; setFocusMinutes: (minutes: number) => void }
export const useUIStore = create<UIState>()(persist((set) => ({ sidebarCollapsed: false, theme: 'system', accent: 'violet', focusMinutes: 25, toggleSidebar: () => set(s => ({ sidebarCollapsed: !s.sidebarCollapsed })), setTheme: theme => set({ theme }), setAccent: accent => set({ accent }), setFocusMinutes: focusMinutes => set({ focusMinutes }) }), { name: 'nivor-preferences' }))
