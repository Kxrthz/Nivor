import { api } from '../api';import type { HealthRecord,HealthSummary } from '../../types/advanced'
export type HealthInput=Omit<HealthRecord,'id'|'createdAt'>
export const healthApi={records:async()=>api.get<HealthRecord[]>('/health/records').then(r=>r.data),create:async(x:HealthInput)=>api.post<HealthRecord>('/health/records',x).then(r=>r.data),update:async(id:number,x:HealthInput)=>api.put<HealthRecord>(`/health/records/${id}`,x).then(r=>r.data),remove:async(id:number)=>api.delete(`/health/records/${id}`),summary:async()=>api.get<HealthSummary>('/health/summary').then(r=>r.data)}
