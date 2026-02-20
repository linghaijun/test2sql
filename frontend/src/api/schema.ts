import { api } from './client'
import type { TableSchema } from '@/types'

export const schemaApi = {
  getTables(connectionId: number): Promise<string[]> {
    return api.get<string[]>(`/schema/${connectionId}/tables`)
  },

  getTableSchema(connectionId: number, tableName: string): Promise<TableSchema> {
    return api.get<TableSchema>(`/schema/${connectionId}/tables/${tableName}`)
  },

  getAllSchemas(connectionId: number): Promise<TableSchema[]> {
    return api.get<TableSchema[]>(`/schema/${connectionId}/schemas`)
  },

  clearCache(connectionId: number): Promise<void> {
    return api.delete(`/schema/${connectionId}/cache`)
  }
}
