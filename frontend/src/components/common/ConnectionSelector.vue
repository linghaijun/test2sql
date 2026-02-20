<template>
  <el-select
    v-model="selectedConnectionId"
    placeholder="选择数据库连接"
    style="width: 240px"
    @change="handleChange"
  >
    <el-option
      v-for="conn in connections"
      :key="conn.id"
      :label="conn.name"
      :value="conn.id"
    >
      <div style="display: flex; align-items: center; justify-content: space-between">
        <span>{{ conn.name }}</span>
        <el-tag :type="conn.type === 'MYSQL' ? 'primary' : 'success'" size="small">
          {{ conn.type }}
        </el-tag>
      </div>
    </el-option>
    
    <template #empty>
      <div style="padding: 10px; text-align: center; color: #909399">
        <el-icon><Warning /></el-icon>
        <p style="margin: 8px 0 0">暂无数据库连接</p>
        <el-button type="primary" size="small" @click="goToDatabase">
          添加连接
        </el-button>
      </div>
    </template>
  </el-select>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useConnectionStore } from '@/stores/connection'
import { storeToRefs } from 'pinia'
import { connectionApi } from '@/api/connection'

const router = useRouter()
const store = useConnectionStore()
const { connections, activeConnectionId } = storeToRefs(store)

const selectedConnectionId = computed({
  get: () => activeConnectionId.value,
  set: (value) => store.setActiveConnection(value)
})

async function loadConnections() {
  try {
    const data = await connectionApi.getAll()
    store.setConnections(data)
  } catch (error) {
    console.error('Failed to load connections:', error)
  }
}

function handleChange(id: number) {
  store.setActiveConnection(id)
}

function goToDatabase() {
  router.push('/database')
}

onMounted(() => {
  loadConnections()
})
</script>
