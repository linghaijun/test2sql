<template>
  <div class="query-input-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>自然语言查询</span>
          <el-button
            type="primary"
            size="small"
            @click="showExamples = !showExamples"
          >
            查看示例
          </el-button>
        </div>
      </template>

      <div v-if="showExamples" class="examples-section">
        <el-tag
          v-for="(example, index) in exampleQueries"
          :key="index"
          class="example-tag"
          @click="useExample(example)"
        >
          {{ example }}
        </el-tag>
      </div>

      <el-input
        v-model="queryText"
        type="textarea"
        :rows="4"
        placeholder="输入您的查询问题，例如：查询所有年龄大于25的员工"
        @keydown.ctrl.enter="submitQuery"
        @keydown.meta.enter="submitQuery"
      />

      <div class="input-footer">
        <div class="char-count">
          {{ queryText.length }} 字符
        </div>
        
        <div class="action-buttons">
          <el-button @click="clearQuery">
            <el-icon><Delete /></el-icon>
            清空
          </el-button>
          
          <el-button
            type="primary"
            :loading="loading"
            :disabled="!queryText.trim() || !hasActiveConnection"
            @click="submitQuery"
          >
            <el-icon><Search /></el-icon>
            生成SQL
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useConnectionStore } from '@/stores/connection'
import { storeToRefs } from 'pinia'
import { nl2sqlApi } from '@/api/nl2sql'

const store = useConnectionStore()
const { activeConnectionId, hasConnections } = storeToRefs(store)

const queryText = ref('')
const loading = ref(false)
const showExamples = ref(false)
const exampleQueries = ref<string[]>([])

const hasActiveConnection = computed(() => hasConnections.value && activeConnectionId.value)

const emit = defineEmits<{
  sqlGenerated: [sql: string]
  error: [message: string]
}>()

onMounted(async () => {
  try {
    exampleQueries.value = await nl2sqlApi.getSuggestions()
  } catch (error) {
    console.error('Failed to load suggestions:', error)
  }
})

function useExample(example: string) {
  queryText.value = example
  showExamples.value = false
}

function clearQuery() {
  queryText.value = ''
}

async function submitQuery() {
  if (!queryText.value.trim()) {
    ElMessage.warning('请输入查询内容')
    return
  }

  if (!activeConnectionId.value) {
    ElMessage.warning('请先选择数据库连接')
    return
  }

  loading.value = true
  
  try {
    const result = await nl2sqlApi.convert(activeConnectionId.value, queryText.value)
    emit('sqlGenerated', result.sql)
    ElMessage.success('SQL生成成功')
  } catch (error: any) {
    const message = error.response?.data?.message || 'SQL生成失败'
    ElMessage.error(message)
    emit('error', message)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.query-input-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.examples-section {
  margin-bottom: 16px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
}

.example-tag {
  margin: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.example-tag:hover {
  opacity: 0.8;
}

.input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

.char-count {
  font-size: 12px;
  color: #909399;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

/* Dark mode */
.dark .examples-section {
  background: #252536;
}

.dark .char-count {
  color: #6b7280;
}
</style>
