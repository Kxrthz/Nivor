import { api } from '../api';import type { Task } from '../../types/task';import type { CalendarEvent } from '../../types/calendar';import type { Goal } from '../../types/goal'
export type Dashboard={taskSummary:{total:number;completed:number;completionPercent:number};todayTasks:Task[];todayEvents:CalendarEvent[];habitSummary:{active:number;completedToday:number;completionPercent:number};goals:Goal[]}
export const dashboardApi={summary:async()=>(await api.get<Dashboard>('/dashboard')).data}
