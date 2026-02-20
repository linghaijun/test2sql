<template>
  <div class="sql-preview-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>生成的SQL</span>
          <div class="header-actions">
            <el-button
              v-if="sql"
              size="small"
              @click="showExplanationDialog = true"
            >
              <el-icon><InfoFilled /></el-icon>
              解释
            </el-button>
            
            <el-button
              v-if="sql"
              size="small"
              @click="copyToClipboard"
            >
              <el-icon><CopyDocument /></el-icon>
              复制
            </el-button>
          </div>
        </div>
      </template>

      <div v-if="!sql" class="empty-state">
        <el-icon :size="48"><Document /></el-icon>
        <p>生成的SQL将显示在这里</p>
      </div>

      <div v-else class="sql-editor">
        <el-input
          v-model="editableSql"
          type="textarea"
          :rows="8"
          @change="handleSqlChange"
        />
        
        <div class="sql-actions">
          <el-button
            type="primary"
            :loading="executing"
            :disabled="!editableSql.trim()"
            @click="executeQuery"
          >
            <el-icon><VideoPlay /></el-icon>
            执行查询
          </el-button>
          
          <el-button
            :disabled="!editableSql.trim()"
            @click="exportResult"
          >
            <el-icon><Download /></el-icon>
            导出CSV
          </el-button>
        </div>
      </div>
    </el-card>

    <el-dialog
      v-model="showExplanationDialog"
      title="SQL解释"
      width="600px"
    >
      <el-skeleton v-if="loadingExplanation" :rows="5" animated />
      <div v-else class="explanation-content">
        {{ explanation }}
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { nl2sqlApi } from '@/api/nl2sql'
import { queryApi } from '@/api/query'
import { useConnectionStore } from '@/stores/connection'
import { storeToRefs } from 'pinia'

const props = defineProps<{
  sql: string
}>()

const emit = defineEmits<{
  queryExecuted: [result: any]
  error: [message: string]
}>()

const store = useConnectionStore()
const { activeConnectionId } = storeToRefs(store)

const editableSql = ref('')
const executing = ref(false)
const showExplanationDialog = ref(false)
const loadingExplanation = ref(false)
const explanation = ref('')

watch(() => props.sql, (newSql) => {
  editableSql.value = newSql
}, { immediate: true })

function handleSqlChange() {
  // SQL已被编辑
}

async function executeQuery() {
  if (!editableSql.value.trim()) {
    ElMessage.warning('请输入SQL语句')
    return
  }

  if (!activeConnectionId.value) {
    ElMessage.warning('请先选择数据库连接')
    return
  }

  executing.value = true
  
  try {
    const result = await queryApi.execute(activeConnectionId.value, editableSql.value)
    emit('queryExecuted', result)
    ElMessage.success('查询执行成功')
  } catch (error: any) {
    const message = error.response?.data?.message || '查询执行失败'
    ElMessage.error(message)
    emit('error', message)
  } finally {
    executing.value = false
  }
}

async function exportResult() {
  if (!editableSql.value.trim() || !activeConnectionId.value) {
    return
  }

  try {
    const csv = await queryApi.export(activeConnectionId.value, editableSql.value)
    
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = `query_result_${Date.now()}.csv`
    link.click()
    URL.revokeObjectURL(link.href)
    
    ElMessage.success('导出成功')
  } catch (error: any) {
    ElMessage.error('导出失败')
  }
}

async function copyToClipboard() {
  try {
    await navigator.clipboard.writeText(editableSql.value)
    ElMessage.success('已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

async function showExplanation() {
  if (!editableSql.value) return
  
  showExplanationDialog.value = true
  loadingExplanation.value = true
  
  try {
    const result = await nl2sqlApi.explain(editableSql.value)
    explanation.value = result.explanation
  } catch (error) {
    ElMessage.error('获取解释失败')
    showExplanationDialog.value = false
  } finally {
    loadingExplanation.value = false
  }
}

watch(showExplanationDialog, (show) => {
  if (show && editableSql.value && !explanation.value) {
    showExplanation()
  }
})
</script>

<style scoped>
.sql-preview-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.empty-state .el-icon {
  color: #c0c4cc;
}

.empty-state p {
  margin-top: 12px;
}

.sql-editor {
  position: relative;
}

.sql-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.explanation-content {
  line-height: 1.6;
  white-space: pre-wrap;
}

/* Dark mode */
.dark .empty-state {
  color: #6b7280;
}

.dark .empty-state .el-icon {
  color: #4b5563;
}

.dark .explanation-content {
  color: #e4e7ed;
}
</style>
