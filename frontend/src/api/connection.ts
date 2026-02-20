import { api } from './client'
import type { DatabaseConnection, CreateConnectionRequest } from '@/types'

export const connectionApi = {
  getAll(): Promise<DatabaseConnection[]> {
    return api.get<DatabaseConnection[]>('/connections')
  },

  getById(id: number): Promise<DatabaseConnection> {
    return api.get<DatabaseConnection>(`/connections/${id}`)
  },

  create(data: CreateConnectionRequest): Promise<DatabaseConnection> {
    return api.post<DatabaseConnection>('/connections', data)
  },

  update(id: number, data: CreateConnectionRequest): Promise<DatabaseConnection> {
    return api.put<DatabaseConnection>(`/connections/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return api.delete(`/connections/${id}`)
  },

  test(id: number): Promise<{ success: boolean; message: string }> {
    return api.post(`/connections/${id}/test`)
  }
}
