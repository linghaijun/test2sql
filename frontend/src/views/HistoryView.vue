<template>
  <AppLayout>
    <div class="history-view">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>查询历史</span>
            <div class="header-actions">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索历史"
                prefix-icon="Search"
                style="width: 200px"
                clearable
                @keyup.enter="searchHistory"
              />
              
              <el-popconfirm
                title="确定要清空所有历史记录吗？"
                @confirm="clearHistory"
              >
                <template #reference>
                  <el-button type="danger">
                    <el-icon><Delete /></el-icon>
                    清空历史
                  </el-button>
                </template>
              </el-popconfirm>
            </div>
          </div>
        </template>

        <el-table :data="historyList" stripe>
          <el-table-column prop="naturalLanguageQuery" label="自然语言查询" min-width="200" />
          
          <el-table-column prop="generatedSql" label="生成的SQL" min-width="300">
            <template #default="{ row }">
              <el-text truncated>{{ row.generatedSql }}</el-text>
            </template>
          </el-table-column>
          
          <el-table-column prop="executed" label="已执行" width="100">
            <template #default="{ row }">
              <el-icon v-if="row.executed" color="#67c23a"><Check /></el-icon>
              <el-icon v-else color="#909399"><Close /></el-icon>
            </template>
          </el-table-column>
          
          <el-table-column prop="resultCount" label="结果数" width="100" />
          
          <el-table-column prop="executionTimeMs" label="执行时间" width="120">
            <template #default="{ row }">
              {{ row.executionTimeMs ? `${row.executionTimeMs}ms` : '-' }}
            </template>
          </el-table-column>
          
          <el-table-column prop="createdAt" label="时间" width="180">
            <template #default="{ row }">
              {{ formatTime(row.createdAt) }}
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button
                size="small"
                @click="rerunQuery(row)"
              >
                重新执行
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadHistory"
          @current-change="loadHistory"
          style="margin-top: 20px; justify-content: flex-end"
        />

        <el-empty v-if="historyList.length === 0" description="暂无查询历史" />
      </el-card>
    </div>
  </AppLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import AppLayout from '@/components/common/AppLayout.vue'
import { historyApi } from '@/api/history'
import type { QueryHistory } from '@/types'

const router = useRouter()

const historyList = ref<QueryHistory[]>([])
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

async function loadHistory() {
  try {
    const response = await historyApi.getAll(undefined, currentPage.value, pageSize.value)
    historyList.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('Failed to load history:', error)
    ElMessage.error('加载历史记录失败')
  }
}

async function searchHistory() {
  if (!searchKeyword.value.trim()) {
    loadHistory()
    return
  }
  
  try {
    const response = await historyApi.search(searchKeyword.value, currentPage.value, pageSize.value)
    historyList.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('Failed to search history:', error)
    ElMessage.error('搜索失败')
  }
}

async function clearHistory() {
  try {
    await historyApi.clear()
    historyList.value = []
    total.value = 0
    ElMessage.success('历史记录已清空')
  } catch (error) {
    console.error('Failed to clear history:', error)
    ElMessage.error('清空失败')
  }
}

function rerunQuery(history: QueryHistory) {
  router.push({
    path: '/query',
    query: {
      query: history.naturalLanguageQuery,
      sql: history.generatedSql
    }
  })
}

function formatTime(time: string) {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}

onMounted(() => {
  loadHistory()
})
</script>

<style scoped>
.history-view {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 12px;
}
</style>
