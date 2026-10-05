import {api} from '../api';import type {User} from '../../types/auth'
export type ProfileInput={name:string;bio:string|null;avatarUrl:string|null}
export const userApi={me:async()=>(await api.get<User>('/users/me')).data,update:async(input:ProfileInput)=>(await api.put<User>('/users/me',input)).data}
