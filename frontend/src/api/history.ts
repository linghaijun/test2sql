import { api } from './client'
import type { QueryHistory, PageResponse } from '@/types'

export const historyApi = {
  getAll(connectionId?: number, page = 1, size = 20): Promise<PageResponse<QueryHistory>> {
    const params = new URLSearchParams()
    if (connectionId) params.append('connectionId', connectionId.toString())
    params.append('page', page.toString())
    params.append('size', size.toString())
    
    return api.get(`/history?${params.toString()}`)
  },

  getRecent(): Promise<QueryHistory[]> {
    return api.get('/history/recent')
  },

  search(keyword: string, page = 1, size = 20): Promise<PageResponse<QueryHistory>> {
    return api.get(`/history/search?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`)
  },

  clear(): Promise<void> {
    return api.delete('/history')
  }
}
