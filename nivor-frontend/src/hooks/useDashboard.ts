import {useQuery} from '@tanstack/react-query';import {dashboardApi} from '../lib/api/dashboardApi';import {queryKeys} from '../lib/queryKeys'
export function useDashboard(){return useQuery({queryKey:queryKeys.dashboard,queryFn:dashboardApi.summary})}
