export interface DatabaseConnection {
  id: number
  name: string
  type: 'MYSQL' | 'POSTGRESQL'
  host: string
  port: number
  databaseName: string
  username: string
  useSsl: boolean
  description?: string
}

export interface CreateConnectionRequest {
  name: string
  type: 'MYSQL' | 'POSTGRESQL'
  host: string
  port: number
  databaseName: string
  username: string
  password: string
  useSsl?: boolean
  description?: string
}

export interface TableSchema {
  tableName: string
  tableComment?: string
  columns: ColumnInfo[]
}

export interface ColumnInfo {
  columnName: string
  columnType: string
  nullable: boolean
  defaultValue?: string
  comment?: string
  isPrimaryKey: boolean
}

export interface QueryResult {
  columns: string[]
  rows: Record<string, any>[]
  totalRows: number
  page?: number
  pageSize?: number
  executionTime?: number
  sql: string
}

export interface QueryHistory {
  id: number
  connectionId: number
  naturalLanguageQuery: string
  generatedSql: string
  executed: boolean
  resultCount?: number
  executionTimeMs?: number
  createdAt: string
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}
