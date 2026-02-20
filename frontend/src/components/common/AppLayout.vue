<template>
  <div class="app-layout">
    <el-container style="height: 100vh">
      <el-aside width="240px" class="sidebar">
        <div class="logo">
          <h2>Text2SQL</h2>
          <p>自然语言转SQL</p>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          router
        >
          <el-menu-item index="/query">
            <el-icon><Search /></el-icon>
            <span>查询</span>
          </el-menu-item>
          
          <el-menu-item index="/database">
            <el-icon><Setting /></el-icon>
            <span>数据库管理</span>
          </el-menu-item>
          
          <el-menu-item index="/history">
            <el-icon><Clock /></el-icon>
            <span>查询历史</span>
          </el-menu-item>
        </el-menu>

        <div class="sidebar-footer">
          <el-divider />
          <p>v1.0.0</p>
        </div>
      </el-aside>

      <el-container>
        <el-header class="app-header">
          <div class="header-left">
            <h3>{{ currentTitle }}</h3>
          </div>
          
          <div class="header-right">
            <ConnectionSelector />
            
            <el-tooltip content="深色模式" placement="bottom">
              <el-switch
                v-model="isDarkMode"
                @change="toggleDarkMode"
                active-icon="Moon"
                inactive-icon="Sunny"
              />
            </el-tooltip>
          </div>
        </el-header>

        <el-main class="app-main">
          <slot></slot>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import ConnectionSelector from './ConnectionSelector.vue'

const route = useRoute()
const isDarkMode = ref(false)

const activeMenu = computed(() => route.path)

const currentTitle = computed(() => {
  const titles: Record<string, string> = {
    '/': '首页',
    '/query': '自然语言查询',
    '/database': '数据库管理',
    '/history': '查询历史'
  }
  return titles[route.path] || 'Text2SQL'
})

function toggleDarkMode(value: boolean) {
  if (value) {
    document.documentElement.classList.add('dark')
  } else {
    document.documentElement.classList.remove('dark')
  }
  localStorage.setItem('darkMode', value.toString())
}

onMounted(() => {
  const savedDarkMode = localStorage.getItem('darkMode')
  if (savedDarkMode === 'true') {
    isDarkMode.value = true
    document.documentElement.classList.add('dark')
  }
})
</script>

<style scoped>
.app-layout {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  flex-direction: column;
}

.logo {
  padding: 24px 20px;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.logo p {
  margin: 4px 0 0;
  font-size: 12px;
  opacity: 0.8;
}

.sidebar-menu {
  border: none;
  background: transparent;
  flex: 1;
}

.sidebar-menu :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  margin: 4px 8px;
}

.sidebar-menu :deep(.el-menu-item:hover),
.sidebar-menu :deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.sidebar-footer {
  padding: 12px;
  text-align: center;
  font-size: 12px;
  opacity: 0.6;
}

.app-header {
  background: white;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.header-left h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.app-main {
  background: #f5f7fa;
  padding: 24px;
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Dark mode styles */
.dark .sidebar {
  background: linear-gradient(180deg, #1e1e2e 0%, #2d2d3f 100%);
}

.dark .app-header {
  background: #1e1e2e;
  border-bottom-color: #2d2d3f;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3);
}

.dark .header-left h3 {
  color: #e4e7ed;
}

.dark .app-main {
  background: #121212;
}

/* Dark mode - Element Plus overrides */
.dark .el-card {
  background: #1e1e2e;
  border-color: #2d2d3f;
  color: #e4e7ed;
}

.dark .el-card__header {
  background: #252536;
  border-bottom-color: #2d2d3f;
  color: #e4e7ed;
}

.dark .el-table {
  background: #1e1e2e;
  color: #e4e7ed;
}

.dark .el-table th.el-table__cell {
  background: #252536;
  color: #e4e7ed;
}

.dark .el-table td.el-table__cell {
  background: #1e1e2e;
  border-bottom-color: #2d2d3f;
}

.dark .el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell {
  background: #252536;
}

.dark .el-table__body tr:hover > td.el-table__cell {
  background: #2d2d3f !important;
}

.dark .el-input__wrapper {
  background: #252536;
  box-shadow: 0 0 0 1px #2d2d3f inset;
}

.dark .el-input__inner {
  color: #e4e7ed;
}

.dark .el-textarea__inner {
  background: #252536;
  border-color: #2d2d3f;
  color: #e4e7ed;
}

.dark .el-button {
  border-color: #2d2d3f;
}

.dark .el-button--default {
  background: #252536;
  border-color: #2d2d3f;
  color: #e4e7ed;
}

.dark .el-button--default:hover {
  background: #2d2d3f;
  border-color: #3d3d4f;
  color: #e4e7ed;
}

.dark .el-form-item__label {
  color: #a3a7b2;
}

.dark .el-select__wrapper {
  background: #252536;
  border-color: #2d2d3f;
}

.dark .el-select__placeholder {
  color: #6b7280;
}

.dark .el-select-dropdown {
  background: #1e1e2e;
  border-color: #2d2d3f;
}

.dark .el-select-dropdown__item {
  color: #e4e7ed;
}

.dark .el-select-dropdown__item:hover {
  background: #2d2d3f;
}

.dark .el-dialog {
  background: #1e1e2e;
  border-color: #2d2d3f;
}

.dark .el-dialog__header {
  background: #252536;
  border-bottom-color: #2d2d3f;
}

.dark .el-dialog__title {
  color: #e4e7ed;
}

.dark .el-dialog__body {
  color: #e4e7ed;
}

.dark .el-pagination {
  color: #e4e7ed;
}

.dark .el-pagination button {
  background: #252536;
  color: #e4e7ed;
}

.dark .el-pager li {
  background: #252536;
  color: #e4e7ed;
}

.dark .el-tree {
  background: transparent;
  color: #e4e7ed;
}

.dark .el-tree-node__content:hover {
  background: #2d2d3f;
}

.dark .el-tree-node.is-current > .el-tree-node__content {
  background: #3d3d4f;
}

.dark .el-menu {
  background: transparent;
  border-color: #2d2d3f;
}

.dark .el-menu-item {
  color: rgba(255, 255, 255, 0.8);
}

.dark .el-menu-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.dark .el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.15);
  color: white;
}

.dark .el-tag {
  border-color: transparent;
}

.dark .el-divider {
  border-color: rgba(255, 255, 255, 0.1);
}

.dark .el-empty__description {
  color: #a3a7b2;
}

.dark .el-tooltip__trigger {
  color: #e4e7ed;
}

.dark .el-switch__core {
  background: #2d2d3f;
}

.dark .el-popconfirm__main {
  color: #e4e7ed;
}

</style>
