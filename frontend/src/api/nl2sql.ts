import { api } from './client'

export const nl2sqlApi = {
  convert(connectionId: number, query: string): Promise<{ sql: string }> {
    return api.post('/nl2sql/convert', { connectionId, query })
  },

  explain(sql: string): Promise<{ explanation: string }> {
    return api.post('/nl2sql/explain', { sql })
  },

  getSuggestions(): Promise<string[]> {
    return api.get('/nl2sql/suggestions')
  }
}
