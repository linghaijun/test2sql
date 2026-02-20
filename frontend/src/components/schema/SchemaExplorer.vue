<template>
  <div class="schema-explorer">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据库表结构</span>
          <el-button
            size="small"
            :loading="loading"
            @click="loadSchemas"
          >
            <el-icon><Refresh /></el-icon>
          </el-button>
        </div>
      </template>

      <el-input
        v-model="searchText"
        placeholder="搜索表"
        prefix-icon="Search"
        size="small"
        clearable
        style="margin-bottom: 12px"
      />

      <el-scrollbar max-height="600px">
        <el-tree
          :data="treeData"
          :props="treeProps"
          node-key="id"
          :expand-on-click-node="false"
          :filter-node-method="filterNode"
          @node-click="handleNodeClick"
        >
          <template #default="{ node, data }">
            <div class="tree-node">
              <span class="node-label">
                <el-icon v-if="data.type === 'table'"><Grid /></el-icon>
                <el-icon v-else><Key /></el-icon>
                {{ node.label }}
              </span>
              
              <el-button
                v-if="data.type === 'column'"
                size="small"
                link
                @click.stop="copyToClipboard(node.label)"
              >
                <el-icon><CopyDocument /></el-icon>
              </el-button>
            </div>
          </template>
        </el-tree>
      </el-scrollbar>

      <el-empty v-if="!loading && treeData.length === 0" description="暂无表结构" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { schemaApi } from '@/api/schema'
import type { TableSchema } from '@/types'

const props = defineProps<{
  connectionId: number | null
}>()

const loading = ref(false)
const searchText = ref('')
const schemas = ref<TableSchema[]>([])

const treeProps = {
  children: 'children',
  label: 'label'
}

const treeData = computed(() => {
  return schemas.value.map(schema => ({
    id: schema.tableName,
    label: schema.tableName,
    type: 'table',
    children: schema.columns.map(col => ({
      id: `${schema.tableName}.${col.columnName}`,
      label: col.columnName,
      type: 'column',
      columnType: col.columnType,
      isPrimaryKey: col.isPrimaryKey,
      nullable: col.nullable
    }))
  }))
})

function filterNode(value: string, data: any) {
  if (!value) return true
  return data.label.toLowerCase().includes(value.toLowerCase())
}

async function loadSchemas() {
  if (!props.connectionId) return
  
  loading.value = true
  
  try {
    schemas.value = await schemaApi.getAllSchemas(props.connectionId)
  } catch (error) {
    console.error('Failed to load schemas:', error)
    ElMessage.error('加载表结构失败')
  } finally {
    loading.value = false
  }
}

function handleNodeClick(data: any) {
  // 点击节点时的处理
}

async function copyToClipboard(text: string) {
  try {
    await navigator.clipboard.writeText(text)
    ElMessage.success('已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

watch(() => props.connectionId, (newId) => {
  if (newId) {
    loadSchemas()
  } else {
    schemas.value = []
  }
}, { immediate: true })

onMounted(() => {
  if (props.connectionId) {
    loadSchemas()
  }
})
</script>

<style scoped>
.schema-explorer {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding-right: 8px;
}

.node-label {
  display: flex;
  align-items: center;
  gap: 4px;
}

.el-tree :deep(.el-tree-node__content) {
  height: 32px;
}

/* Dark mode */
.dark .tree-node {
  color: #e4e7ed;
}

.dark .node-label {
  color: #e4e7ed;
}
</style>
