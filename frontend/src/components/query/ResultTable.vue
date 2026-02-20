<template>
  <div class="result-table-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <span>查询结果</span>
            <el-tag v-if="totalRows > 0" type="success" size="small">
              {{ totalRows }} 行
            </el-tag>
            <el-tag v-if="executionTime" type="info" size="small">
              {{ executionTime }}ms
            </el-tag>
          </div>
          
          <div class="header-right">
            <el-input
              v-model="filterText"
              placeholder="过滤结果"
              prefix-icon="Search"
              size="small"
              style="width: 200px"
              clearable
            />
          </div>
        </div>
      </template>

      <div v-if="!hasData" class="empty-state">
        <el-icon :size="48" color="#c0c4cc"><Document /></el-icon>
        <p>查询结果将显示在这里</p>
      </div>

      <div v-else-if="totalRows === 0" class="empty-state">
        <el-icon :size="48"><Warning /></el-icon>
        <p>查询无结果</p>
      </div>

      <el-table
        v-else
        :data="filteredRows"
        :default-sort="{ prop: columns[0], order: 'ascending' }"
        stripe
        border
        max-height="500"
        style="width: 100%"
      >
        <el-table-column
          v-for="column in columns"
          :key="column"
          :prop="column"
          :label="column"
          sortable
          min-width="120"
        >
          <template #default="{ row }">
            <span v-if="row[column] === null" class="null-value">NULL</span>
            <span v-else>{{ row[column] }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = defineProps<{
  columns: string[]
  rows: Record<string, any>[]
  totalRows: number
  executionTime?: number
}>()

const filterText = ref('')

const hasData = computed(() => props.columns.length > 0)

const filteredRows = computed(() => {
  if (!filterText.value) {
    return props.rows
  }
  
  const filter = filterText.value.toLowerCase()
  return props.rows.filter(row => {
    return Object.values(row).some(value => {
      if (value === null || value === undefined) return false
      return String(value).toLowerCase().includes(filter)
    })
  })
})
</script>

<style scoped>
.result-table-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.empty-state p {
  margin-top: 12px;
}

.null-value {
  color: #909399;
  font-style: italic;
}

/* Dark mode */
.dark .empty-state {
  color: #6b7280;
}

.dark .empty-state .el-icon {
  color: #d97706;
}

.dark .null-value {
  color: #4b5563;
}
</style>
