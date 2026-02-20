import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { DatabaseConnection } from '@/types'

export const useConnectionStore = defineStore('connection', () => {
  const connections = ref<DatabaseConnection[]>([])
  const activeConnectionId = ref<number | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const activeConnection = computed(() => {
    return connections.value.find(c => c.id === activeConnectionId.value) || null
  })

  const hasConnections = computed(() => connections.value.length > 0)

  function setConnections(data: DatabaseConnection[]) {
    connections.value = data
    if (!activeConnectionId.value && data.length > 0) {
      activeConnectionId.value = data[0].id
    }
  }

  function setActiveConnection(id: number) {
    activeConnectionId.value = id
  }

  function addConnection(connection: DatabaseConnection) {
    connections.value.push(connection)
  }

  function updateConnection(connection: DatabaseConnection) {
    const index = connections.value.findIndex(c => c.id === connection.id)
    if (index !== -1) {
      connections.value[index] = connection
    }
  }

  function removeConnection(id: number) {
    connections.value = connections.value.filter(c => c.id !== id)
    if (activeConnectionId.value === id) {
      activeConnectionId.value = connections.value[0]?.id || null
    }
  }

  function setLoading(value: boolean) {
    loading.value = value
  }

  function setError(value: string | null) {
    error.value = value
  }

  function clearError() {
    error.value = null
  }

  return {
    connections,
    activeConnectionId,
    loading,
    error,
    activeConnection,
    hasConnections,
    setConnections,
    setActiveConnection,
    addConnection,
    updateConnection,
    removeConnection,
    setLoading,
    setError,
    clearError
  }
})
