<template>
  <AppLayout>
    <div class="database-view">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>数据库连接管理</span>
            <el-button type="primary" @click="showAddDialog">
              <el-icon><Plus /></el-icon>
              添加连接
            </el-button>
          </div>
        </template>

        <el-table :data="connections" stripe>
          <el-table-column prop="name" label="名称" width="180" />
          
          <el-table-column prop="type" label="类型" width="120">
            <template #default="{ row }">
              <el-tag :type="row.type === 'MYSQL' ? 'primary' : 'success'">
                {{ row.type }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="host" label="主机" width="200" />
          
          <el-table-column prop="port" label="端口" width="100" />
          
          <el-table-column prop="databaseName" label="数据库" width="180" />
          
          <el-table-column prop="useSsl" label="SSL" width="80">
            <template #default="{ row }">
              <el-icon v-if="row.useSsl" color="#67c23a"><Check /></el-icon>
              <el-icon v-else color="#909399"><Close /></el-icon>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="280">
            <template #default="{ row }">
              <el-button
                size="small"
                :loading="testingId === row.id"
                @click="testConnection(row.id)"
              >
                测试
              </el-button>
              
              <el-button
                size="small"
                @click="editConnection(row)"
              >
                编辑
              </el-button>
              
              <el-popconfirm
                title="确定要删除这个连接吗？"
                @confirm="deleteConnection(row.id)"
              >
                <template #reference>
                  <el-button
                    size="small"
                    type="danger"
                  >
                    删除
                  </el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>

        <el-empty v-if="connections.length === 0" description="暂无数据库连接" />
      </el-card>

      <el-dialog
        v-model="dialogVisible"
        :title="editingId ? '编辑连接' : '添加连接'"
        width="600px"
      >
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" placeholder="连接名称" />
          </el-form-item>
          
          <el-form-item label="数据库类型" prop="type">
            <el-select v-model="form.type" style="width: 100%">
              <el-option label="MySQL" value="MYSQL" />
              <el-option label="PostgreSQL" value="POSTGRESQL" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="主机" prop="host">
            <el-input v-model="form.host" placeholder="localhost" />
          </el-form-item>
          
          <el-form-item label="端口" prop="port">
            <el-input-number
              v-model="form.port"
              :min="1"
              :max="65535"
              style="width: 100%"
            />
          </el-form-item>
          
          <el-form-item label="数据库名" prop="databaseName">
            <el-input v-model="form.databaseName" placeholder="database_name" />
          </el-form-item>
          
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="root" />
          </el-form-item>
          
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="password"
              show-password
            />
          </el-form-item>
          
          <el-form-item label="使用SSL">
            <el-switch v-model="form.useSsl" />
          </el-form-item>
          
          <el-form-item label="描述">
            <el-input
              v-model="form.description"
              type="textarea"
              :rows="2"
              placeholder="连接描述（可选）"
            />
          </el-form-item>
        </el-form>

        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button
            type="primary"
            :loading="saving"
            @click="saveConnection"
          >
            保存
          </el-button>
        </template>
      </el-dialog>
    </div>
  </AppLayout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import AppLayout from '@/components/common/AppLayout.vue'
import { connectionApi } from '@/api/connection'
import { useConnectionStore } from '@/stores/connection'
import type { DatabaseConnection, CreateConnectionRequest } from '@/types'

const store = useConnectionStore()

const connections = ref<DatabaseConnection[]>([])
const dialogVisible = ref(false)
const editingId = ref<number | null>(null)
const testingId = ref<number | null>(null)
const saving = ref(false)
const formRef = ref<FormInstance>()

const form = reactive<CreateConnectionRequest>({
  name: '',
  type: 'MYSQL',
  host: 'localhost',
  port: 3306,
  databaseName: '',
  username: 'root',
  password: '',
  useSsl: false,
  description: ''
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入连接名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择数据库类型', trigger: 'change' }],
  host: [{ required: true, message: '请输入主机地址', trigger: 'blur' }],
  port: [{ required: true, message: '请输入端口号', trigger: 'blur' }],
  databaseName: [{ required: true, message: '请输入数据库名', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function loadConnections() {
  try {
    const data = await connectionApi.getAll()
    connections.value = data
    store.setConnections(data)
  } catch (error) {
    console.error('Failed to load connections:', error)
  }
}

function showAddDialog() {
  editingId.value = null
  resetForm()
  dialogVisible.value = true
}

function editConnection(connection: DatabaseConnection) {
  editingId.value = connection.id
  Object.assign(form, {
    name: connection.name,
    type: connection.type,
    host: connection.host,
    port: connection.port,
    databaseName: connection.databaseName,
    username: connection.username,
    password: '',
    useSsl: connection.useSsl,
    description: connection.description || ''
  })
  dialogVisible.value = true
}

async function testConnection(id: number) {
  testingId.value = id
  
  try {
    const result = await connectionApi.test(id)
    if (result.success) {
      ElMessage.success('连接成功')
    } else {
      ElMessage.error('连接失败')
    }
  } catch (error: any) {
    const message = error.response?.data?.message || '连接测试失败'
    ElMessage.error(message)
  } finally {
    testingId.value = null
  }
}

async function saveConnection() {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    saving.value = true
    
    try {
      if (editingId.value) {
        const updated = await connectionApi.update(editingId.value, form)
        store.updateConnection(updated)
        ElMessage.success('更新成功')
      } else {
        const created = await connectionApi.create(form)
        store.addConnection(created)
        ElMessage.success('创建成功')
      }
      
      dialogVisible.value = false
      await loadConnections()
    } catch (error: any) {
      const message = error.response?.data?.message || '保存失败'
      ElMessage.error(message)
    } finally {
      saving.value = false
    }
  })
}

async function deleteConnection(id: number) {
  try {
    await connectionApi.delete(id)
    store.removeConnection(id)
    ElMessage.success('删除成功')
    await loadConnections()
  } catch (error: any) {
    const message = error.response?.data?.message || '删除失败'
    ElMessage.error(message)
  }
}

function resetForm() {
  Object.assign(form, {
    name: '',
    type: 'MYSQL',
    host: 'localhost',
    port: 3306,
    databaseName: '',
    username: 'root',
    password: '',
    useSsl: false,
    description: ''
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadConnections()
})
</script>

<style scoped>
.database-view {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
