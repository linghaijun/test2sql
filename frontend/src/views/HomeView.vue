<template>
  <AppLayout>
    <div class="home-view">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card class="welcome-card">
            <template #header>
              <h2>欢迎使用 Text2SQL</h2>
            </template>
            
            <p class="description">
              Text2SQL 是一个基于 AI 的自然语言转 SQL 查询系统。您可以使用自然语言描述查询需求，
              系统会自动将其转换为可执行的 SQL 语句。
            </p>

            <el-divider />

            <div class="features">
              <h3>主要功能</h3>
              <el-row :gutter="16">
                <el-col :span="8">
                  <el-card shadow="hover" class="feature-card">
                    <el-icon :size="32" color="#409eff"><Search /></el-icon>
                    <h4>自然语言查询</h4>
                    <p>使用中文或英文描述查询需求</p>
                  </el-card>
                </el-col>
                
                <el-col :span="8">
                  <el-card shadow="hover" class="feature-card">
                    <el-icon :size="32" color="#67c23a"><Database /></el-icon>
                    <h4>多数据库支持</h4>
                    <p>支持 MySQL、PostgreSQL 等主流数据库</p>
                  </el-card>
                </el-col>
                
                <el-col :span="8">
                  <el-card shadow="hover" class="feature-card">
                    <el-icon :size="32" color="#e6a23c"><Clock /></el-icon>
                    <h4>查询历史</h4>
                    <p>保存查询历史，方便重复使用</p>
                  </el-card>
                </el-col>
              </el-row>
            </div>

            <el-divider />

            <div class="quick-start">
              <h3>快速开始</h3>
              <el-steps :active="currentStep" align-center>
                <el-step title="添加数据库" description="配置数据库连接" />
                <el-step title="输入查询" description="使用自然语言描述查询" />
                <el-step title="执行查询" description="查看和导出结果" />
              </el-steps>
              
              <div class="action-buttons">
                <el-button
                  v-if="!hasConnections"
                  type="primary"
                  size="large"
                  @click="goToDatabase"
                >
                  添加数据库连接
                </el-button>
                
                <el-button
                  v-else
                  type="primary"
                  size="large"
                  @click="goToQuery"
                >
                  开始查询
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="stats-card">
            <template #header>
              <h3>系统状态</h3>
            </template>
            
            <el-statistic title="数据库连接" :value="connectionsCount">
              <template #suffix>个</template>
            </el-statistic>
            
            <el-divider />
            
            <el-statistic title="AI 模型" value="通义千问">
              <template #suffix>Plus</template>
            </el-statistic>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </AppLayout>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import AppLayout from '@/components/common/AppLayout.vue'
import { useConnectionStore } from '@/stores/connection'
import { storeToRefs } from 'pinia'

const router = useRouter()
const store = useConnectionStore()
const { hasConnections, connections } = storeToRefs(store)

const connectionsCount = computed(() => connections.value.length)

const currentStep = computed(() => {
  if (!hasConnections.value) return 0
  return 1
})

function goToDatabase() {
  router.push('/database')
}

function goToQuery() {
  router.push('/query')
}
</script>

<style scoped>
.home-view {
  padding: 20px;
}

.welcome-card h2 {
  margin: 0;
  color: #303133;
}

.description {
  font-size: 16px;
  line-height: 1.8;
  color: #606266;
}

.features h3,
.quick-start h3,
.stats-card h3 {
  margin: 0 0 16px;
  color: #303133;
}

.feature-card {
  text-align: center;
  padding: 20px 0;
}

.feature-card h4 {
  margin: 12px 0 8px;
  color: #303133;
}

.feature-card p {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.action-buttons {
  margin-top: 32px;
  text-align: center;
}

.stats-card :deep(.el-statistic) {
  text-align: center;
}
</style>
