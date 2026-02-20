<template>
  <AppLayout>
    <div class="query-view">
      <el-row :gutter="20">
        <el-col :span="18">
          <QueryInput
            @sql-generated="handleSqlGenerated"
            @error="handleError"
          />
          
          <SQLPreview
            :sql="generatedSql"
            @query-executed="handleQueryExecuted"
            @error="handleError"
          />
          
          <ResultTable
            :columns="resultColumns"
            :rows="resultRows"
            :total-rows="resultTotalRows"
            :execution-time="resultExecutionTime"
          />
        </el-col>

        <el-col :span="6">
          <SchemaExplorer :connection-id="activeConnectionId" />
        </el-col>
      </el-row>
    </div>
  </AppLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import AppLayout from '@/components/common/AppLayout.vue'
import QueryInput from '@/components/query/QueryInput.vue'
import SQLPreview from '@/components/query/SQLPreview.vue'
import ResultTable from '@/components/query/ResultTable.vue'
import SchemaExplorer from '@/components/schema/SchemaExplorer.vue'
import { useConnectionStore } from '@/stores/connection'
import { storeToRefs } from 'pinia'

const store = useConnectionStore()
const { activeConnectionId } = storeToRefs(store)

const generatedSql = ref('')
const resultColumns = ref<string[]>([])
const resultRows = ref<Record<string, any>[]>([])
const resultTotalRows = ref(0)
const resultExecutionTime = ref<number | undefined>()

function handleSqlGenerated(sql: string) {
  generatedSql.value = sql
  // 清空之前的结果
  resultColumns.value = []
  resultRows.value = []
  resultTotalRows.value = 0
  resultExecutionTime.value = undefined
}

function handleQueryExecuted(result: any) {
  resultColumns.value = result.columns || []
  resultRows.value = result.rows || []
  resultTotalRows.value = result.totalRows || 0
  resultExecutionTime.value = result.executionTime
}

function handleError(message: string) {
  console.error('Error:', message)
}
</script>

<style scoped>
.query-view {
  padding: 20px;
}
</style>
