import { api } from '../api';import type { SearchResult } from '../../types/advanced'
export const searchApi={search:async(q:string)=>api.get<{results:SearchResult[]}>('/search',{params:{q,limit:8}}).then(r=>r.data.results)}
