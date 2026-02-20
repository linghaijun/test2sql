import { api } from './client'
import type { QueryResult } from '@/types'

export const queryApi = {
  execute(connectionId: number, sql: string, page?: number, pageSize?: number): Promise<QueryResult> {
    return api.post<QueryResult>('/query/execute', {
      connectionId,
      sql,
      page,
      pageSize
    })
  },

  export(connectionId: number, sql: string): Promise<string> {
    return api.post('/query/execute/export', { connectionId, sql })
  }
}
