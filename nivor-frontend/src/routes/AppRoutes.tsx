import { lazy, Suspense } from 'react'
import { Route, Routes } from 'react-router-dom'
import { AppLayout } from '../components/layout/AppLayout'
import { AuthLayout } from '../layouts/AuthLayout'
import { LandingLayout } from '../layouts/LandingLayout'
import { ProtectedRoute } from './ProtectedRoute'
import { PublicOnlyRoute } from './PublicOnlyRoute'
const HomePage = lazy(() => import('../pages/app/HomePage').then(m => ({ default: m.HomePage })))
const SectionPage = lazy(() => import('../pages/app/SectionPage').then(m => ({ default: m.SectionPage })))
const NotFoundPage = lazy(() => import('../pages/errors/NotFoundPage').then(m => ({ default: m.NotFoundPage })))
const AuthPage = lazy(() => import('../pages/app/AuthPages').then(m => ({ default: m.AuthPage })))
const LandingPage = lazy(() => import('../pages/landing/LandingPage').then(m => ({ default: m.LandingPage })))
const TasksPage = lazy(() => import('../pages/app/DataPages').then(m => ({ default: m.TasksPage })))
const PlannerPage = lazy(() => import('../pages/app/DataPages').then(m => ({ default: m.PlannerPage })))
const NotesPage = lazy(() => import('../pages/app/DataPages').then(m => ({ default: m.NotesPage })))
const GoalsPage = lazy(() => import('../pages/app/DataPages').then(m => ({ default: m.GoalsPage })))
const HabitsPage = lazy(() => import('../pages/app/DataPages').then(m => ({ default: m.HabitsPage })))
const JournalPage = lazy(() => import('../pages/app/DataPages').then(m => ({ default: m.JournalPage })))
const WorkspacePage = lazy(() => import('../pages/app/DataPages').then(m => ({ default: m.WorkspacePage })))
const AdvancedPage = lazy(() => import('../pages/app/AdvancedPages').then(m => ({ default: m.AdvancedPage })))
const ProfilePage = lazy(() => import('../pages/app/DataPages').then(m => ({ default: m.ProfilePage })))
const Loading = () => <div className="route-loading"><i/> Preparing your space...</div>
export function AppRoutes() { return <Suspense fallback={<Loading/>}><Routes><Route element={<LandingLayout/>}><Route path="/" element={<LandingPage/>}/></Route><Route element={<PublicOnlyRoute/>}><Route element={<AuthLayout/>}><Route path="/login" element={<AuthPage/>}/><Route path="/register" element={<AuthPage/>}/></Route></Route><Route element={<ProtectedRoute/>}><Route element={<AppLayout/>}><Route path="/home" element={<HomePage/>}/><Route path="/app" element={<HomePage/>}/><Route path="/planner" element={<PlannerPage/>}/><Route path="/ai" element={<AdvancedPage/>}/><Route path="/workspace" element={<WorkspacePage/>}/><Route path="/notes" element={<NotesPage/>}/><Route path="/tasks" element={<TasksPage/>}/><Route path="/goals" element={<GoalsPage/>}/><Route path="/habits" element={<HabitsPage/>}/><Route path="/journal" element={<JournalPage/>}/><Route path="/files" element={<AdvancedPage/>}/><Route path="/finance" element={<AdvancedPage/>}/><Route path="/health" element={<AdvancedPage/>}/><Route path="/focus" element={<AdvancedPage/>}/><Route path="/analytics" element={<AdvancedPage/>}/><Route path="/profile" element={<ProfilePage/>}/><Route path="/settings" element={<SectionPage/>}/></Route></Route><Route path="*" element={<NotFoundPage/>}/></Routes></Suspense> }
